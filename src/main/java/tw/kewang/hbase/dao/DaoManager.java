package tw.kewang.hbase.dao;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoManager {
	private static final Logger LOG = LoggerFactory.getLogger(DaoManager.class);
	private static final HashMap<Class<? extends AbstractDao>, AbstractDao> DAO_MAPS = new HashMap<Class<? extends AbstractDao>, AbstractDao>();

	public static <T extends AbstractDao> AbstractDao getInstance(Class<T> clazz) {
		AbstractDao dao = DAO_MAPS.get(clazz);

		if (dao == null) {
			try {
				dao = clazz.newInstance();

				DAO_MAPS.put(clazz, dao);

				return dao;
			} catch (Exception e) {
				LOG.error(Constants.EXCEPTION_PREFIX, e);

				throw new RuntimeException("Can't create an instance");
			}
		} else {
			return dao;
		}
	}
}