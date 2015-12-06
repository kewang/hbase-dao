package tw.kewang.hbase.dao;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Domain;
import tw.kewang.hbase.annotations.Table;
import tw.kewang.hbase.domain.AbstractDomain;

public abstract class AbstractDao {
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractDao.class);
	private static final HConnection CONNECTION = HConnectionHolder
			.getInstance().getHConnection();

	private final Table table = getTableAnnotation();

	public AbstractDomain getByRowkey(String rowkey) {
		HTableInterface hTableInterface = null;

		try {
			hTableInterface = CONNECTION.getTable(table.name());

			Result result = hTableInterface.get(buildGet(rowkey));

			if (!result.isEmpty()) {
				String resultRowkey = Bytes.toString(result.getRow());

				for (Class<? extends AbstractDomain> domain : table.domains()) {
					Domain domainAnnotation = domain
							.getAnnotation(Domain.class);

					String rowkeyPattern = domainAnnotation.rowkey();

					buildDomain(domain, rowkeyPattern, resultRowkey);
				}
			}
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		} catch (Error e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		} finally {
			closeHTable(hTableInterface);
		}

		return null;
	}

	private Table getTableAnnotation() {
		Class<?> clazz = getClass();

		Table table = clazz.getAnnotation(Table.class);

		if (table == null) {
			throw new RuntimeException("No table annotation");
		}

		return table;
	}

	private Get buildGet(String rowkey) {
		Get get = new Get(Bytes.toBytes(rowkey));

		return get;
	}

	private String buildDomain(Class<?> clazz, String rowkeyPattern,
			String rowkeyRowkey) {
		Pattern pattern = Pattern.compile(rowkeyPattern);
		Matcher matcher = pattern.matcher(rowkeyRowkey);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);

				Component component = field.getAnnotation(Component.class);

				if (component.name().equals(matcher.group(1))) {
					try {
						String value = castValue(field);

						if (value != null) {
							matcher.appendReplacement(sb, value);
						} else {
							return null;
						}
					} catch (Exception e) {
						LOG.error(Constants.EXCEPTION_PREFIX, e);

						return null;
					}
				}
			}
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	private String castValue(Field field) {
		Class<?> fieldClass = field.getType();

		try {
			if (fieldClass.isAssignableFrom(String.class)) {
				return (String) field.get(this);
			} else if (fieldClass.isAssignableFrom(Long.class)) {
				return String.valueOf(field.get(this));
			} else if (fieldClass.isAssignableFrom(Integer.class)) {
				return String.valueOf(field.get(this));
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		}

		return null;
	}

	private void closeHTable(HTableInterface hTableInterface) {
		if (hTableInterface != null) {
			try {
				hTableInterface.close();
			} catch (Exception e) {
				LOG.error(Constants.EXCEPTION_PREFIX, e);
			} catch (Error e) {
				LOG.error(Constants.EXCEPTION_PREFIX, e);
			}
		}
	}
}