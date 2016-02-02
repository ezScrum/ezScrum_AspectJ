package ntut.csie.ezScrum.web.dataObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ntut.csie.ezScrum.dao.SerialNumberDAO;
import ntut.csie.ezScrum.issue.sql.service.core.Configuration;
import ntut.csie.ezScrum.issue.sql.service.core.InitialSQL;
import ntut.csie.ezScrum.issue.sql.service.tool.internal.MySQLControl;
import ntut.csie.ezScrum.web.databaseEnum.SerialNumberEnum;

public class SerialNumberObjectTest {
	private MySQLControl mControl = null;
	private Configuration mConfig;

	@Before
	public void setUp() {
		mConfig = new Configuration();
		mConfig.setTestMode(true);
		mConfig.save();

		InitialSQL ini = new InitialSQL(mConfig);
		ini.exe();

		mControl = new MySQLControl(mConfig);
		mControl.connect();
	}

	@After
	public void tearDown() {
		InitialSQL ini = new InitialSQL(mConfig);
		ini.exe();

		// 讓 config 回到  Production 模式
		mConfig.setTestMode(false);
		mConfig.save();

		ini = null;
		mControl = null;
		mConfig = null;
	}

	@Test
	public void testToJSON() throws JSONException {
		long projectId = 1;
		long releaseId = 2;
		long sprintId = 3;
		long storyId = 4;
		long taskId = 5;
		long unplanId = 6;
		long retrospectiveId = 7;
		
		SerialNumberObject serialNumber = new SerialNumberObject(projectId);
		serialNumber.setReleaseId(releaseId)
		            .setSprintId(sprintId)
		            .setStoryId(storyId)
		            .setTaskId(taskId)
		            .setUnplanId(unplanId)
		            .setRetrospectiveId(retrospectiveId)
		            .save();
		
		JSONObject serialNumberJson = serialNumber.toJSON();
		assertEquals(serialNumber.getId(), serialNumberJson.getLong(SerialNumberEnum.ID));
		assertEquals(serialNumber.getProjectId(), serialNumberJson.getLong(SerialNumberEnum.PROJECT_ID));
		assertEquals(serialNumber.getReleaseId(), serialNumberJson.getLong(SerialNumberEnum.RELEASE));
		assertEquals(serialNumber.getSprintId(), serialNumberJson.getLong(SerialNumberEnum.SPRINT));
		assertEquals(serialNumber.getStoryId(), serialNumberJson.getLong(SerialNumberEnum.STORY));
		assertEquals(serialNumber.getTaskId(), serialNumberJson.getLong(SerialNumberEnum.TASK));
		assertEquals(serialNumber.getUnplanId(), serialNumberJson.getLong(SerialNumberEnum.UNPLAN));
		assertEquals(serialNumber.getRetrospectiveId(), serialNumberJson.getLong(SerialNumberEnum.RETROSPECTIVE));
	}

	@Test
	public void testSave_CreateANewSerialNumber() {
		// Test Data
		long projectId = 1;
		long releaseId = 2;
		long sprintId = 3;
		long storyId = 4;
		long taskId = 5;
		long unplanId = 6;
		long retrospectiveId = 7;

		// Build SerialNumberObject
		SerialNumberObject serialNumber = new SerialNumberObject(projectId);
		serialNumber.setReleaseId(releaseId)
		            .setRetrospectiveId(retrospectiveId)
		            .setSprintId(sprintId)
		            .setStoryId(storyId)
		            .setTaskId(taskId)
		            .setUnplanId(unplanId)
		            .save();

		long id = serialNumber.getId();
		assertTrue(id > 0);

		SerialNumberObject serialNumberFromDB = SerialNumberDAO.getInstance().get(id);

		assertEquals(id, serialNumberFromDB.getId());
		assertEquals(projectId, serialNumberFromDB.getProjectId());
		assertEquals(releaseId, serialNumberFromDB.getReleaseId());
		assertEquals(sprintId, serialNumberFromDB.getSprintId());
		assertEquals(storyId, serialNumberFromDB.getStoryId());
		assertEquals(taskId, serialNumberFromDB.getTaskId());
		assertEquals(unplanId, serialNumberFromDB.getUnplanId());
		assertEquals(retrospectiveId, serialNumberFromDB.getRetrospectiveId());
	}
	
	@Test
	public void testSave_UpdateSerialNumber() {
		// Test Data
		long projectId = 1;
		long releaseId = 12;
		long sprintId = 13;
		long storyId = 14;
		long taskId = 15;
		long unplanId = 16;
		long retrospectiveId = 17;

		// Build SerialNumberObject
		SerialNumberObject serialNumber = new SerialNumberObject(projectId);
		serialNumber.setReleaseId(2)
		            .setSprintId(3)
		            .setStoryId(4)
		            .setTaskId(5)
		            .setUnplanId(6)
		            .setRetrospectiveId(7)
		            .save();

		long id = serialNumber.getId();
		assertTrue(id > 0);
		assertEquals(id, serialNumber.getId());
		assertEquals(1, serialNumber.getProjectId());
		assertEquals(2, serialNumber.getReleaseId());
		assertEquals(3, serialNumber.getSprintId());
		assertEquals(4, serialNumber.getStoryId());
		assertEquals(5, serialNumber.getTaskId());
		assertEquals(6, serialNumber.getUnplanId());
		assertEquals(7, serialNumber.getRetrospectiveId());
		
		// Update serialNumber
		serialNumber.setReleaseId(releaseId)
		            .setRetrospectiveId(retrospectiveId)
		            .setSprintId(sprintId)
		            .setStoryId(storyId)
		            .setTaskId(taskId)
		            .setUnplanId(unplanId)
		            .save();

		SerialNumberObject serialNumberFromDB = SerialNumberDAO.getInstance().get(id);
		
		// Assert
		assertEquals(id, serialNumberFromDB.getId());
		assertEquals(projectId, serialNumberFromDB.getProjectId());
		assertEquals(releaseId, serialNumberFromDB.getReleaseId());
		assertEquals(sprintId, serialNumberFromDB.getSprintId());
		assertEquals(storyId, serialNumberFromDB.getStoryId());
		assertEquals(taskId, serialNumberFromDB.getTaskId());
		assertEquals(unplanId, serialNumberFromDB.getUnplanId());
		assertEquals(retrospectiveId, serialNumberFromDB.getRetrospectiveId());
	}

	@Test
	public void testReload() {
		// Test Data
		long projectId = 1;
		long releaseId = 2;
		long sprintId = 3;
		long storyId = 4;
		long taskId = 5;
		long unplanId = 6;
		long retrospectiveId = 7;

		// Build SerialNumberObject
		SerialNumberObject serialNumber = new SerialNumberObject(projectId);
		serialNumber.setReleaseId(releaseId)
		            .setRetrospectiveId(retrospectiveId)
		            .setSprintId(sprintId)
		            .setStoryId(storyId)
		            .setTaskId(taskId)
		            .setUnplanId(unplanId)
		            .save();
		
		assertEquals(projectId, serialNumber.getProjectId());
		assertEquals(releaseId, serialNumber.getReleaseId());
		assertEquals(sprintId, serialNumber.getSprintId());
		assertEquals(storyId, serialNumber.getStoryId());
		assertEquals(taskId, serialNumber.getTaskId());
		assertEquals(unplanId, serialNumber.getUnplanId());
		assertEquals(retrospectiveId, serialNumber.getRetrospectiveId());
		
		long releaseId2 = 7;
		long sprintId2 = 6;
		long storyId2 = 5;
		long taskId2 = 4;
		long unplanId2 = 3;
		long retrospectiveId2 = 2;
		
		serialNumber.setReleaseId(releaseId2)
		            .setSprintId(sprintId2)
		            .setStoryId(storyId2)
		            .setTaskId(taskId2)
		            .setUnplanId(unplanId2)
		            .setRetrospectiveId(retrospectiveId2);
		
		assertEquals(releaseId2, serialNumber.getReleaseId());
		assertEquals(sprintId2, serialNumber.getSprintId());
		assertEquals(storyId2, serialNumber.getStoryId());
		assertEquals(taskId2, serialNumber.getTaskId());
		assertEquals(unplanId2, serialNumber.getUnplanId());
		assertEquals(retrospectiveId2, serialNumber.getRetrospectiveId());
		
		serialNumber.reload();
		assertEquals(projectId, serialNumber.getProjectId());
		assertEquals(releaseId, serialNumber.getReleaseId());
		assertEquals(sprintId, serialNumber.getSprintId());
		assertEquals(storyId, serialNumber.getStoryId());
		assertEquals(taskId, serialNumber.getTaskId());
		assertEquals(unplanId, serialNumber.getUnplanId());
		assertEquals(retrospectiveId, serialNumber.getRetrospectiveId());
	}

	@Test
	public void testDelete() {
		// Test Data
		long projectId = 1;
		long releaseId = 2;
		long sprintId = 3;
		long storyId = 4;
		long taskId = 5;
		long unplanId = 6;
		long retrospectiveId = 7;

		// Build SerialNumberObject
		SerialNumberObject serialNumber = new SerialNumberObject(projectId);
		serialNumber.setReleaseId(releaseId)
		            .setRetrospectiveId(retrospectiveId)
		            .setSprintId(sprintId)
		            .setStoryId(storyId)
		            .setTaskId(taskId)
		            .setUnplanId(unplanId)
		            .save();
		
		assertTrue(serialNumber.exists());
		assertTrue(serialNumber.delete());
		assertFalse(serialNumber.exists());
	}

	@Test
	public void testExists() {
		// Test Data
		long projectId = 1;
		long releaseId = 2;
		long sprintId = 3;
		long storyId = 4;
		long taskId = 5;
		long unplanId = 6;
		long retrospectiveId = 7;

		// Build SerialNumberObject
		SerialNumberObject serialNumber = new SerialNumberObject(projectId);
		serialNumber.setReleaseId(releaseId)
		            .setRetrospectiveId(retrospectiveId)
		            .setSprintId(sprintId)
		            .setStoryId(storyId)
		            .setTaskId(taskId)
		            .setUnplanId(unplanId)
		            .save();
		
		assertTrue(serialNumber.exists());
		assertTrue(serialNumber.delete());
		assertFalse(serialNumber.exists());
	}
}
