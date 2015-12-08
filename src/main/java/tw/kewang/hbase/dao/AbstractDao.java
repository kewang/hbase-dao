package tw.kewang.hbase.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
				for (Class<? extends AbstractDomain> domain : table.domains()) {
					Domain domainAnnotation = domain
							.getAnnotation(Domain.class);

					String rowkeyPattern = domainAnnotation.rowkey();

					buildDomain(domain, rowkeyPattern, rowkey);
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
			String rowkey) {
		char[] patternChars = rowkeyPattern.toCharArray();
		char[] rowkeyChars = rowkey.toCharArray();
		boolean foundField = false;
		StringBuffer fieldName = new StringBuffer();
		StringBuffer fieldValue = new StringBuffer();
		int patternIndex = 0;
		int rowkeyIndex = 0;
		ArrayList<DomainField> domainFields = new ArrayList<DomainField>();

		while (true) {
			char patternChar = patternChars[patternIndex];

			switch (patternChar) {
			case '{':
				foundField = true;

				break;
			case '}':
				LOG.debug(fieldName.toString());

				char separator = patternChars[patternIndex + 1];

				for (; rowkeyIndex < rowkeyChars.length; rowkeyIndex++) {
					char rowkeyChar = rowkeyChars[rowkeyIndex];

					if (rowkeyChar == separator) {
						break;
					}

					fieldValue.append(rowkeyChar);
				}

				domainFields.add(new DomainField(fieldName.toString(),
						fieldValue.toString()));

				fieldName.setLength(0);
				fieldValue.setLength(0);

				foundField = false;

				patternIndex++;

				break;
			default:
				if (foundField) {
					fieldName.append(patternChar);
				}

				break;
			}

			patternIndex++;
		}
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

	private class DomainField {
		private String fieldName;
		private String fieldValue;

		public DomainField(String fieldName, String fieldValue) {
			this.fieldName = fieldName;
			this.fieldValue = fieldValue;
		}

		public String getFieldName() {
			return fieldName;
		}

		public String getFieldValue() {
			return fieldValue;
		}
	}
}