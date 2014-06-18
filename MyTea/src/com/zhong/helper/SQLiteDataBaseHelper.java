package com.zhong.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
//OPEN_READONLY ：代表的是以只读方式打开数据库（常量值为：1）
//OPEN_READWRITE：代表以读写方式打开数据库（常量值为：0）
//CREATE_IF_NECESSARY：当数据库不存在时创建数据库
//NO_LOCALIZED_COLLATORS：打开数据库时，不根据本地化语言对数据库进行排序（常量值为：16）
//mDatabaseHelper.getWritableDatabase();不推荐作用，如果磁盘满了还是会继续写入会产生异常
//用到的权限
//向sdcard写入权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
//在sdcard中创建与删除文件的权限<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
//注意数据库的可写权限！！！
//linux下的权限
//-rwx------: 文件所有者对文件具有读取、写入和执行的权限。
//-rwxr-—r--: 文件所有者具有读、写与执行的权限，其他用户则具有读取的权限。
//-rw-rw-r-x: 文件所有者与同组用户对文件具有读写的权限，而其他用户仅具有读取和执行的权限。
//drwx--x--x: 目录所有者具有读写与进入目录的权限,其他用户近能进入该目录，却无法读取任何数据。
//Drwx------: 除了目录所有者具有完整的权限之外，其他用户对该目录完全没有任何权限。
//r(Read，读取)：对文件而言，具有读取文件内容的权限；对目录来说，具有浏览目 录的权限。
//w(Write,写入)：对文件而言，具有新增、修改文件内容的权限；对目录来说，具有删除、移动目录内文件的权限。
//x(eXecute，执行)：对文件而言，具有执行文件的权限；对目录了来说该用户具有进入目录的权限。
//如果关闭执行权限，则表示字符会变成大写：
//-rwSr-Sr-T 1 root root 4096 6月 23 08：17 conf
//注意表名和库名的不同
//SQLiteDatabase有自带的insert、update、delete、query四个方法虽然方法，但不利于熟SQL语句。因为不封装

/**
 * 自己封装的数据库操作类
 * 
 * @author 仲丛旭
 * 
 */
public class SQLiteDataBaseHelper {
	/** 日志打印 */
	private static final String TAG = "SQLiteDataBaseHelper";
	/** 用于管理和操作SQLite数据库 */
	private SQLiteDatabase database = null;
	/** 由SQLiteOpenHelper继承过来，用于实现数据库的建立与更新 */
	private MySQLiteOpen mySQLiteOpen = null;
	// ================================
	/** SD卡的根目录 */
	private final String SDCARD_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	/** 打开默认数据库路径 */
	private final String PATH = SDCARD_ROOT + File.separator + "zhong"
			+ File.separator + "tea.db";
	// ==============================
	/** 要创建的数据库名字 */
	private static final String DB_NAME = "words.db";
	/** 数据库版本 */
	private static final int VERSION = 1;
	/** 创建表名 */
//	private static final String SQL_CREATE_TABLE = "CREATE TABLE tb_words(_id INTEGER PRIMARY KEY AUTOINCREMENT , english , chinese)";
	private static final String SQL_CREATE_TABLE = "CREATE TABLE tb_teacontents(_id INTEGER PRIMARY KEY, title , source , create_time , type)";

	/**
	 * 继承SQLiteOpenHelper类，在构造方法中分别需要传入Context,数据库名称,CursorFactory(一般传入null
	 * 
	 * 为默认数据库),数据库版本号(不能为负数)。在SQLiteOpenHelper中首先执行的是onCreate方法
	 * 
	 * 在构造函数时并没有真正创建数据库
	 * 
	 * 而在调用getWritableDatabase或者getReadableDatabase方法时才真正去创建数据库
	 * 
	 * 返回一个SQLiteDatabase对象。
	 * 
	 * 数据存储到了data/data/应用包名/databases
	 * 
	 * @author 仲丛旭
	 * 
	 */
	private class MySQLiteOpen extends SQLiteOpenHelper {
		/**
		 * 构造方法
		 * 
		 * @param context
		 * @param name
		 * @param factory
		 * @param version
		 */
		public MySQLiteOpen(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			Log.i(TAG, "==MySQLiteOpen()");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "==数据库没有表时创建一个");
			db.execSQL(SQL_CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(TAG, "==升级数据库");
			if (newVersion > oldVersion) {
				db.execSQL("DROP TABLE IF EXISTS tb_words");
				onCreate(db);
			}
		}

	}

	/**
	 * 默认的构造方法自动去创建一个默认的数据库路径(SD卡的数据，需要到这个文来修改)
	 */
	public SQLiteDataBaseHelper() {
		database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * 创建数据库，返回数据库对象
	 * 
	 * @param context
	 */
	public SQLiteDataBaseHelper(Context context, String name) {
		mySQLiteOpen = new MySQLiteOpen(context, DB_NAME, null, VERSION);
		database = mySQLiteOpen.getReadableDatabase();
		// database = mySQLiteOpen.getWritableDatabase();
	}

	/**
	 * @作用 查询数据返回Cursor
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Cursor selectCursor(String sql, String[] selectionArgs) {
		return database.rawQuery(sql, selectionArgs);
	}

	/**
	 * @作用 执行带占位符的select语句，返回list集合
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public List<Map<String, String>> SelectData(String sql,
			String[] selectionArgs) {
		Cursor cursor = selectCursor(sql, selectionArgs);
		return cursorToList(cursor);
	}

	/**
	 * @作用 已知一个cursor得到List集合
	 * @param cursor
	 * @return
	 */
	private List<Map<String, String>> cursorToList(Cursor cursor) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] arrColumnName = cursor.getColumnNames();
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < arrColumnName.length; i++) {
				String cols_value = cursor.getString(i);
				map.put(arrColumnName[i], cols_value);
			}
			list.add(map);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	/**
	 * @作用 执行带占位符的update、insert、delete语句，更新数据库，返回true或false
	 * @param sql
	 * @param bindArgs
	 *            问号中的参数值
	 * @return boolean
	 */
	public boolean updataData(String sql, Object[] bindArgs) {
		try {
			if (bindArgs == null) {
				database.execSQL(sql);
			} else {
				database.execSQL(sql, bindArgs);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @作用 执行带占位符的select语句，返回结果集的个数。如果已经查询过了不推荐继续使用，占内存
	 * @param sql
	 * @param selectionArgs
	 * @return int
	 */
	public int selectCount(String sql, String[] selectionArgs) {
		Cursor cursor = database.rawQuery(sql, selectionArgs);
		int count = 0;
		if (cursor != null) {
			count = cursor.getCount();
			cursor.close();
		}
		return count;
	}

	/**
	 * @作用 关闭数据库操作类
	 */
	public void destroy() {
		if (mySQLiteOpen != null) {
			mySQLiteOpen.close();
			mySQLiteOpen = null;
		}
		if (database != null) {
			database.close();
			database = null;
		}
	}
}
