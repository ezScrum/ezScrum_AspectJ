package ntut.csie.ezScrum.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ntut.csie.ezScrum.issue.sql.service.core.IQueryValueSet;
import ntut.csie.ezScrum.issue.sql.service.internal.MySQLQuerySet;
import ntut.csie.ezScrum.web.dataObject.ReleaseObject;
import ntut.csie.ezScrum.web.dataObject.SerialNumberObject;
import ntut.csie.ezScrum.web.databaseEnum.ReleaseEnum;
import ntut.csie.jcis.core.util.DateUtil;

public class ReleaseDAO extends AbstractDAO<ReleaseObject, ReleaseObject> {

	private static ReleaseDAO sInstance = null;

	public static ReleaseDAO getInstance() {
		if (sInstance == null) {
			sInstance = new ReleaseDAO();
		}
		return sInstance;
	}

	@Override
	public long create(ReleaseObject release) {
		long currentTime = System.currentTimeMillis();

		IQueryValueSet valueSet = new MySQLQuerySet();
		SerialNumberObject serialNumber = SerialNumberDAO.getInstance().get(
				release.getProjectId());

		long releaseSerialId = serialNumber.getReleaseId() + 1;

		valueSet.addTableName(ReleaseEnum.TABLE_NAME);
		valueSet.addInsertValue(ReleaseEnum.SERIAL_ID, releaseSerialId);
		valueSet.addInsertValue(ReleaseEnum.NAME, release.getName());
		valueSet.addInsertValue(ReleaseEnum.DESCRIPTION,
				release.getDescription());
		valueSet.addInsertValue(ReleaseEnum.START_DATE,
				release.getStartDateString());
		valueSet.addInsertValue(ReleaseEnum.DUE_DATE,
				release.getDueDateString());
		valueSet.addInsertValue(ReleaseEnum.PROJECT_ID, release.getProjectId());
		valueSet.addInsertValue(ReleaseEnum.CREATE_TIME, currentTime);
		valueSet.addInsertValue(ReleaseEnum.UPDATE_TIME, currentTime);

		String query = valueSet.getInsertQuery();
		long id = mControl.executeInsert(query);

		serialNumber.setReleaseId(releaseSerialId);
		serialNumber.save();

		return id;
	}

	@Override
	public ReleaseObject get(long id) {
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(ReleaseEnum.TABLE_NAME);
		valueSet.addEqualCondition(ReleaseEnum.ID, id);

		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);

		ReleaseObject release = null;
		try {
			if (result.next()) {
				release = convert(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return release;
	}

	@Override
	public boolean update(ReleaseObject release) {
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(ReleaseEnum.TABLE_NAME);
		valueSet.addInsertValue(ReleaseEnum.NAME, release.getName());
		valueSet.addInsertValue(ReleaseEnum.DESCRIPTION, release.getDescription());
		valueSet.addInsertValue(ReleaseEnum.START_DATE, release.getStartDateString());
		valueSet.addInsertValue(ReleaseEnum.DUE_DATE, release.getDueDateString());
		valueSet.addInsertValue(ReleaseEnum.UPDATE_TIME, release.getUpdateTime());
		valueSet.addEqualCondition(ReleaseEnum.ID, release.getId());
		String query = valueSet.getUpdateQuery();
		
		return mControl.executeUpdate(query);
	}

	@Override
	public boolean delete(long id) {
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(ReleaseEnum.TABLE_NAME);
		valueSet.addEqualCondition(ReleaseEnum.ID, id);
		String query = valueSet.getDeleteQuery();
		
		return mControl.executeUpdate(query);
	}
	
	public ArrayList<ReleaseObject> getReleasesByProjectId(long projectId) {
		ArrayList<ReleaseObject> releases = new ArrayList<ReleaseObject>();

		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(ReleaseEnum.TABLE_NAME);
		valueSet.addEqualCondition(ReleaseEnum.PROJECT_ID, projectId);

		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);

		try {
			while (result.next()) {
				releases.add(convert(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
		}
		return releases;
	}

	public static ReleaseObject convert(ResultSet result) throws SQLException {
		ReleaseObject release = new ReleaseObject(
				result.getLong(ReleaseEnum.ID),
				result.getLong(ReleaseEnum.SERIAL_ID),
				result.getLong(ReleaseEnum.PROJECT_ID));
		release.setName(result.getString(ReleaseEnum.NAME))
				.setDescription(result.getString(ReleaseEnum.DESCRIPTION))
				.setStartDate(new SimpleDateFormat(DateUtil._8DIGIT_DATE_1).format(result.getDate(ReleaseEnum.START_DATE)))
				.setDueDate(new SimpleDateFormat(DateUtil._8DIGIT_DATE_1).format(result.getDate(ReleaseEnum.DUE_DATE)))
				.setCreateTime(result.getLong(ReleaseEnum.CREATE_TIME))
				.setUpdateTime(result.getLong(ReleaseEnum.UPDATE_TIME));
		return release;
	}
}
