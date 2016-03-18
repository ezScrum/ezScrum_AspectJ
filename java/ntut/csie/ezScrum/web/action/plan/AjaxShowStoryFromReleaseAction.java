package ntut.csie.ezScrum.web.action.plan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ntut.csie.ezScrum.web.action.PermissionAction;
import ntut.csie.ezScrum.web.dataObject.ProjectObject;
import ntut.csie.ezScrum.web.dataObject.ReleaseObject;
import ntut.csie.ezScrum.web.helper.ReleasePlanHelper;
import ntut.csie.ezScrum.web.support.SessionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class AjaxShowStoryFromReleaseAction extends PermissionAction {
	private static Log log = LogFactory.getLog(AjaxShowStoryFromReleaseAction.class);
	
	@Override
	public boolean isValidAction() {
		return super.getScrumRole().getAccessReleasePlan();
	}

	@Override
	public boolean isXML() {
		// XML
		return true;
	}
	
	@Override
	public StringBuilder getResponse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(" Show Story From Release. ");
		
		// get session info
		ProjectObject project = (ProjectObject) SessionManager.getProject(request);
		
		ReleasePlanHelper planHelper = new ReleasePlanHelper(project);
		
		// get parameter info
		String serialReleaseIdString = request.getParameter("Rid");
		long serialReleaseId = Long.parseLong(serialReleaseIdString);
		// Get release id
		long releaseId = -1;
		ReleaseObject release = ReleaseObject.get(project.getId(), serialReleaseId);
		if (release != null) {
			releaseId = release.getId();
		}
		// 取得 ReleasePlan
		return planHelper.showStoryFromRelease(releaseId);
	}
}
