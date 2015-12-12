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
				return findDomain(rowkey);
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

	private AbstractDomain findDomain(String rowkey) throws Exception {
		for (Class<? extends AbstractDomain> domainClass : table.domains()) {
			Domain domainAnnotation = domainClass.getAnnotation(Domain.class);
			ArrayList<DomainField> domainFields = buildDomain(domainClass,
					domainAnnotation.rowkey(), rowkey);

			if (domainFields == null) {
				continue;
			}

			for (DomainField domainField : domainFields) {
				boolean foundField = findField(domainClass, domainField);

				if (!foundField) {
					continue;
				}
			}

			AbstractDomain domain = domainClass.newInstance();

			for (Field field : domain.getClass().getDeclaredFields()) {
				field.setAccessible(true);

				Component component = field.getAnnotation(Component.class);

				for (DomainField domainField : domainFields) {
					if (checkName(domainField.fieldName, component)) {
						field.set(domain, domainField.fieldValue);

						break;
					}
				}
			}

			return domain;
		}

		return null;
	}

	private boolean findField(Class<? extends AbstractDomain> domainClass,
			DomainField domainField) {
		for (Field field : domainClass.getDeclaredFields()) {
			field.setAccessible(true);

			Component component = field.getAnnotation(Component.class);

			if (checkName(domainField.fieldName, component)) {
				return true;
			}
		}

		return false;
	}

	private boolean checkName(String fieldName, Component component) {
		return component != null && component.name().equals(fieldName);
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

	private ArrayList<DomainField> buildDomain(Class<?> clazz, String pattern,
			String rowkey) {
		char[] patternChars = pattern.toCharArray();
		char[] rowkeyChars = rowkey.toCharArray();
		boolean foundField = false;
		StringBuffer fieldName = new StringBuffer();
		StringBuffer fieldValue = new StringBuffer();
		StringBuffer mid = new StringBuffer();
		int patternIndex = 0;
		int rowkeyIndex = 0;
		ArrayList<DomainField> domainFields = new ArrayList<DomainField>();

		while (true) {
			char patternChar = patternChars[patternIndex];

			switch (patternChar) {
			case '{':
				foundField = true;

				String midString = mid.toString();

				int searchIndex = rowkey.indexOf(midString, rowkeyIndex);

				if (searchIndex == -1) {
					return null;
				}

				rowkeyIndex = searchIndex + midString.length();

				mid.setLength(0);

				break;
			case '}':
				LOG.debug(fieldName.toString());

				char separator;

				if (patternIndex == patternChars.length - 1) {
					separator = '\n';
				} else {
					separator = patternChars[patternIndex + 1];
				}

				for (; rowkeyIndex < rowkeyChars.length; rowkeyIndex++) {
					char rowkeyChar;

					if (rowkeyIndex == rowkeyChars.length) {
						rowkeyChar = '\n';
					} else {
						rowkeyChar = rowkeyChars[rowkeyIndex];
					}

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

				break;
			default:
				if (foundField) {
					fieldName.append(patternChar);
				} else {
					mid.append(patternChar);
				}

				break;
			}

			if (patternIndex == patternChars.length - 1) {
				break;
			}

			patternIndex++;
		}

		return domainFields;
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

	public static void main(String[] args) {
		String rowkey = "xyz_abc";
		String mid = "_";
		int rowkeyIndex = 3;

		rowkeyIndex = rowkey.indexOf(mid.toString(), rowkeyIndex)
				+ mid.toString().length();

		System.out.println(rowkeyIndex);
	}

	private class DomainField {
		private String fieldName;
		private String fieldValue;

		public DomainField(String fieldName, String fieldValue) {
			this.fieldName = fieldName;
			this.fieldValue = fieldValue;
		}
	}
}