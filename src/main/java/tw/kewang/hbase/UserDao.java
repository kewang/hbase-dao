package tw.kewang.hbase;

import tw.kewang.hbase.annotations.Table;
import tw.kewang.hbase.dao.AbstractDao;

@Table(name = "User", domains = { User1.class })
public class UserDao extends AbstractDao {
}