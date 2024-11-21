/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.itasoft.tmmin.eform.am.plugin;

import id.co.itasoft.tmmin.eform.am.plugin.dao.GetApproverMappingDao;
import id.co.itasoft.tmmin.eform.am.plugin.model.Approver;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginWebSupport;
import org.joget.workflow.model.service.WorkflowUserManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tarkiman
 */

/*
*___________________________________________________________________________
* HIRARKI
*
*OPR/TM -> GH/TL -> LH/GL -----|                               <<<<-FACTORY
*                     |
*                     V
*            STAFF -> SH -> DPH -> DH   -> BOD -> VPD -> PD    <<<<-OFFICE
*                           PM     DDH-P
*                           SO     DDH
*___________________________________________________________________________
*  OPR	: OPERATOR || TM : TEAM MEMBER
*  GH	: GROUP HEAD || TL : TEAM LEADER
*  LH	: LINE HEAD || GL : GROUP LEADER
*  	
*  SH	: SECTION HEAD
*  DPH	: DEPARTMENT HEAD
*  DH	: DIVISION HEAD
*  BOD	: BOARD OF DIRECTOR
*  VPD	: VICE PRESIDENT
*  PD	: PRESIDENT DIRECTOR
*__________________________________________________________________________
 */

 /*
TL = http://203.190.54.75:81/jw/web/json/plugin/id.co.itasoft.tmmin.eform.am.plugin.GetApproverMapping/service?username=sulaiman

TM = http://203.190.54.75:81/jw/web/json/plugin/id.co.itasoft.tmmin.eform.am.plugin.GetApproverMapping/service?username=supriyono
TL = http://203.190.54.75:81/jw/web/json/plugin/id.co.itasoft.tmmin.eform.am.plugin.GetApproverMapping/service?username=karna
GL = http://203.190.54.75:81/jw/web/json/plugin/id.co.itasoft.tmmin.eform.am.plugin.GetApproverMapping/service?username=kusmiyanto
 */
public class GetApproverMapping extends Element implements PluginWebSupport {

    public static String pluginName = "TMMIN - AM - Get Approver Mapping";
    GetApproverMappingDao dao = new GetApproverMappingDao();

    @Override
    public String renderTemplate(FormData fd, Map map) {
        return "";
    }

    @Override
    public String getName() {
        return pluginName;
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getDescription() {
        return pluginName;
    }

    @Override
    public String getLabel() {
        return pluginName;
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return "";
    }

    @Override
    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        WorkflowUserManager workflowUserManager = (WorkflowUserManager) AppUtil.getApplicationContext().getBean("workflowUserManager");

        JSONObject mainObj = new JSONObject();

        /*Cek Hanya User Yang Sudah Login Bisa Mengakses Ini*/
        if (!workflowUserManager.isCurrentUserAnonymous()) {

            Approver currentUser = new Approver();

            if (request.getParameterMap().containsKey("current_user_username")) {
                String currentUserUsername = request.getParameter("current_user_username");
                currentUser = dao.getDetailCurrentUserByUsername(currentUserUsername);
            } else if (request.getParameterMap().containsKey("current_user_noreg")) {
                String currentUserNoreg = request.getParameter("current_user_noreg");
                currentUser = dao.getDetailCurrentUserByNoreg(currentUserNoreg);
            } 
//            else {
//                try {
//                    mainObj.put("error", "undefined current noreg or username");
//                    mainObj.write(response.getWriter());
//                    return;
//                } catch (JSONException ex) {
//                    LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
//                }
//            }

            Approver employee = new Approver();

            if (request.getParameterMap().containsKey("username")) {
                String username = request.getParameter("username");
                employee = dao.getDetailEmployeeByUsername(username);
            } else if (request.getParameterMap().containsKey("noreg")) {
                String noreg = request.getParameter("noreg");
                employee = dao.getDetailEmployeeByNoreg(noreg);
            } else if(request.getParameterMap().containsKey("section_id")){
                String sectionId = request.getParameter("section_id");
                employee = dao.getAnEmployeeBySection(sectionId);
            }
//            else {
//                try {
//                    mainObj.put("error", "undefined noreg or username");
//                    mainObj.write(response.getWriter());
//                    return;
//                } catch (JSONException ex) {
//                    LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
//                }
//            }

            if (employee != null) {

                try {

                    if (currentUser != null) {

                        JSONObject objCurrentUser = new JSONObject();
                        objCurrentUser.put("username", currentUser.getUsername());
                        objCurrentUser.put("noreg", currentUser.getNoreg());
                        objCurrentUser.put("name", currentUser.getName());
                        objCurrentUser.put("position", currentUser.getPositionAbbr());
                        objCurrentUser.put("class", currentUser.getClasS());
                        objCurrentUser.put("email", currentUser.getEmail());

                        objCurrentUser.put("division_id", currentUser.getDivId());
                        objCurrentUser.put("division_name", currentUser.getDivName());
                        objCurrentUser.put("department_id", currentUser.getDeptId());
                        objCurrentUser.put("department_name", currentUser.getDeptName());
                        objCurrentUser.put("section_id", currentUser.getSectionId());
                        objCurrentUser.put("section_name", currentUser.getSectionName());
                        objCurrentUser.put("cost_center", currentUser.getCostCenter());
                        objCurrentUser.put("ext", currentUser.getExt());

                        mainObj.put("current_user", objCurrentUser);
                    }

                    JSONObject objEmployee = new JSONObject();
                    objEmployee.put("username", employee.getUsername());
                    objEmployee.put("class_cd", employee.getClassCd());
                    objEmployee.put("noreg", employee.getNoreg());
                    objEmployee.put("name", employee.getName());
                    objEmployee.put("class", employee.getClasS());
                    objEmployee.put("email", employee.getEmail());
                    objEmployee.put("position_abbr", employee.getPositionAbbr());
                    objEmployee.put("labor_type", employee.getLaborType());
                    objEmployee.put("div_id", employee.getDivId());
                    objEmployee.put("div_name", employee.getDivName());
                    objEmployee.put("section_id", employee.getSectionId());
                    objEmployee.put("section_name", employee.getSectionName());
                    objEmployee.put("dept_id", employee.getDeptId());
                    objEmployee.put("dept_name", employee.getDeptName());
                    objEmployee.put("cost_center", employee.getCostCenter());
                    objEmployee.put("ext", employee.getExt());
                    objEmployee.put("present_status", employee.getPresentStatus());
                    objEmployee.put("main_location", employee.getMainLocation());
                    objEmployee.put("status", employee.getStatus());
                    objEmployee.put("date_of_employee", employee.getDateOfEmployee());
                    objEmployee.put("place_of_birth", employee.getPlaceOfBirth());
                    objEmployee.put("dob", employee.getDateOfBirth());
                    objEmployee.put("expired_date", employee.getExpiredDate());
                    objEmployee.put("gender", employee.getGender());
                    objEmployee.put("street_address", employee.getStreetAddress());
                    objEmployee.put("terlambat", employee.getTerlambat());
                    objEmployee.put("sakit_opname", employee.getSakitOpname());
                    objEmployee.put("sakit_surat_dokter", employee.getSakitSuratDokter());
                    objEmployee.put("mangkir", employee.getMangkir());
                    objEmployee.put("pulcep_dengan_ijin", employee.getPulcepDenganIjin());
                    objEmployee.put("pulcep_tanpa_ijin", employee.getPulcepTanpaIjin());
                    objEmployee.put("unit_code", employee.getUnitCode());
                    objEmployee.put("directorate_id", employee.getDirectorateId());

//                    String str_dob = employee.getDateOfBirth();
//                    LocalDate dob = LocalDate.parse(str_dob);
//                    objEmployee.put("age", "" + LoadDataEmployee.calculateAge(dob));

                    if(request.getParameterMap().containsKey("section_id")){
                        mainObj.put("sample_employee", objEmployee);
                    } else{
                        mainObj.put("employee", objEmployee);
                    }

                    List<Approver> listApprover = new ArrayList<Approver>();
                    listApprover = this.getAllApprover(employee);

                    JSONArray list = new JSONArray();
                    JSONObject objApprover;
                    StringBuilder messageError = new StringBuilder();
                    for (Approver r : listApprover) {
                        if (!r.isError()) {
                            objApprover = new JSONObject();
                            objApprover.put("username", r.getUsername());
                            objApprover.put("noreg", r.getNoreg());
                            objApprover.put("name", r.getName());
                            objApprover.put("position", r.getPositionAbbr());
                            objApprover.put("class", r.getClasS());
                            objApprover.put("email", r.getEmail());
                            list.put(objApprover);
                        } else {
                            messageError.append(r.getMessage() + ";");
                        }
                    }
                    mainObj.put("list_approvers", list);
                    String msgError = messageError.toString();
                    //msgError = msgError.substring(0, msgError.length() - 1);
                    mainObj.put("message", msgError);

                    JSONObject objHR = new JSONObject();

                    /*Dapetin APPROVER HR_ER_SH*/
 /*
                    Section    : Employment Regulation & Uni Relation Sec.
                    Department : Employee Relations Dept.
                    Div        : Human Resources Div.
                     */
//                    Approver HR_ER_SH = new Approver();
//                    HR_ER_SH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_ER_SH");
//
//                    /*JSON Response HR_ER_SH*/
//                    JSONObject objHR_ER_SH = new JSONObject();
//                    objHR_ER_SH.put("username", HR_ER_SH.getUsername());
//                    objHR_ER_SH.put("noreg", HR_ER_SH.getNoreg());
//                    objHR_ER_SH.put("name", HR_ER_SH.getName());
//                    objHR_ER_SH.put("position", HR_ER_SH.getPositionAbbr());
//                    objHR_ER_SH.put("class", HR_ER_SH.getClasS());
//                    objHR_ER_SH.put("email", HR_ER_SH.getEmail());
//                    objHR.put("approver_hr_er_sh", objHR_ER_SH);
//
//                    /*Dapetin APPROVER HR_PTM_SH*/
// /*
//                    Section    : 
//                    Department : 
//                    Div        : Human Resources Div.
//                     */
//                    Approver HR_PTM_SH = new Approver();
//                    HR_PTM_SH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_PTM_SH");
//
//                    /*JSON Response HR_PTM_SH*/
//                    JSONObject objHR_PTM_SH = new JSONObject();
//                    objHR_PTM_SH.put("username", HR_PTM_SH.getUsername());
//                    objHR_PTM_SH.put("noreg", HR_PTM_SH.getNoreg());
//                    objHR_PTM_SH.put("name", HR_PTM_SH.getName());
//                    objHR_PTM_SH.put("position", HR_PTM_SH.getPositionAbbr());
//                    objHR_PTM_SH.put("class", HR_PTM_SH.getClasS());
//                    objHR_PTM_SH.put("email", HR_PTM_SH.getEmail());
//                    objHR.put("approver_hr_ptm_sh", objHR_PTM_SH);
//
//                    /*Dapetin APPROVER HR_PTM_DPH*/
// /*
//                    Section    : 
//                    Department : 
//                    Div        : Human Resources Div.
//                     */
//                    Approver HR_PTM_DPH = new Approver();
//                    HR_PTM_DPH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_PTM_DPH");
//
//                    /*JSON Response HR_PTM_DPH*/
//                    JSONObject objHR_PTM_DPH = new JSONObject();
//                    objHR_PTM_DPH.put("username", HR_PTM_DPH.getUsername());
//                    objHR_PTM_DPH.put("noreg", HR_PTM_DPH.getNoreg());
//                    objHR_PTM_DPH.put("name", HR_PTM_DPH.getName());
//                    objHR_PTM_DPH.put("position", HR_PTM_DPH.getPositionAbbr());
//                    objHR_PTM_DPH.put("class", HR_PTM_DPH.getClasS());
//                    objHR_PTM_DPH.put("email", HR_PTM_DPH.getEmail());
//                    objHR.put("approver_hr_ptm_dph", objHR_PTM_DPH);
//
//                    /*Dapetin APPROVER HR_ER_DPH*/
// /*
//                    Section    : 
//                    Department : 
//                    Div        : Human Resources Div.
//                     */
//                    Approver HR_ER_DPH = new Approver();
//                    HR_ER_DPH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_ER_DPH");
//
//                    /*JSON Response HR_ER_DPH*/
//                    JSONObject objHR_ER_DPH = new JSONObject();
//                    objHR_ER_DPH.put("username", HR_ER_DPH.getUsername());
//                    objHR_ER_DPH.put("noreg", HR_ER_DPH.getNoreg());
//                    objHR_ER_DPH.put("name", HR_ER_DPH.getName());
//                    objHR_ER_DPH.put("position", HR_ER_DPH.getPositionAbbr());
//                    objHR_ER_DPH.put("class", HR_ER_DPH.getClasS());
//                    objHR_ER_DPH.put("email", HR_ER_DPH.getEmail());
//                    objHR.put("approver_hr_er_dph", objHR_ER_DPH);

                    /*Dapetin APPROVER HR_DH*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
                    Approver HR_DH = new Approver();
                    HR_DH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_DH");

                    String ttdHrDH = "";
                    if (!"".equalsIgnoreCase(HR_DH.getNoreg())) {
                        ttdHrDH = dao.getTtd(HR_DH.getNoreg());
                    }

                    /*JSON Response HR_DH*/
                    JSONObject objHR_DH = new JSONObject();
                    objHR_DH.put("username", HR_DH.getUsername());
                    objHR_DH.put("noreg", HR_DH.getNoreg());
                    objHR_DH.put("name", HR_DH.getName());
                    objHR_DH.put("position", HR_DH.getPositionAbbr());
                    objHR_DH.put("class", HR_DH.getClasS());
                    objHR_DH.put("email", HR_DH.getEmail());
                    objHR_DH.put("ext", HR_DH.getExt());
                    objHR_DH.put("ttd_hr_dh", ttdHrDH);
                    objHR.put("approver_hr_dh", objHR_DH);
                    
                    
                    /*Dapetkan APPROVER HR_DPH*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
                    Approver HR_DPH = new Approver();
                    HR_DPH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_DPH");

                    String ttdHrDPH = "";
                    if (!"".equalsIgnoreCase(HR_DPH.getNoreg())) {
                        ttdHrDPH = dao.getTtd(HR_DPH.getNoreg());
                    }

                    /*JSON Response HR_DH*/
                    JSONObject objHR_DPH = new JSONObject();
                    objHR_DPH.put("username", HR_DPH.getUsername());
                    objHR_DPH.put("noreg", HR_DPH.getNoreg());
                    objHR_DPH.put("name", HR_DPH.getName());
                    objHR_DPH.put("position", HR_DPH.getPositionAbbr());
                    objHR_DPH.put("class", HR_DPH.getClasS());
                    objHR_DPH.put("email", HR_DPH.getEmail());
                    objHR_DPH.put("ext", HR_DPH.getExt());
                    objHR_DPH.put("ttd_hr_dph", ttdHrDPH);
                    objHR.put("approver_hr_dph", objHR_DPH);                    


                    /*Dapetin APPROVER HR_REM*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
//                    Approver HR_REM = new Approver();
//                    HR_REM = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_REM");
//
//                    /*JSON Response HR_DH*/
//                    JSONObject objHR_REM = new JSONObject();
//                    objHR_REM.put("username", HR_REM.getUsername());
//                    objHR_REM.put("noreg", HR_REM.getNoreg());
//                    objHR_REM.put("name", HR_REM.getName());
//                    objHR_REM.put("position", HR_REM.getPositionAbbr());
//                    objHR_REM.put("class", HR_REM.getClasS());
//                    objHR_REM.put("email", HR_REM.getEmail());
//                    objHR.put("approver_hr_rem", objHR_REM);
//
//                    /*Dapetin APPROVER HR_ER_STAFF*/
// /*
//                    Section    : 
//                    Department : 
//                    Div        : Human Resources Div.
//                     */
//                    Approver HR_ER_STAFF = new Approver();
//                    HR_ER_STAFF = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_ER_STAFF");
//
//                    /*JSON Response HR_ER_STAFF*/
//                    JSONObject objHR_ER_STAFF = new JSONObject();
//                    objHR_ER_STAFF.put("username", HR_ER_STAFF.getUsername());
//                    objHR_ER_STAFF.put("noreg", HR_ER_STAFF.getNoreg());
//                    objHR_ER_STAFF.put("name", HR_ER_STAFF.getName());
//                    objHR_ER_STAFF.put("position", HR_ER_STAFF.getPositionAbbr());
//                    objHR_ER_STAFF.put("class", HR_ER_STAFF.getClasS());
//                    objHR_ER_STAFF.put("email", HR_ER_STAFF.getEmail());
//                    objHR.put("approver_hr_er_staff", objHR_ER_STAFF);
//
//                    /*Dapetin APPROVER HR_PGM*/
// /*
//                    Section    : 
//                    Department : 
//                    Div        : Human Resources Div.
//                     */
//                    Approver HR_PGM = new Approver();
//                    HR_PGM = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_PGM");
//
//                    String ttdHrPgm = "";
//                    if (!"".equalsIgnoreCase(HR_PGM.getNoreg())) {
//                        ttdHrPgm = dao.getTtd(HR_PGM.getNoreg());
//                    }
//                    /*JSON Response HR_PGM*/
//                    JSONObject objHR_PGM = new JSONObject();
//                    objHR_PGM.put("username", HR_PGM.getUsername());
//                    objHR_PGM.put("noreg", HR_PGM.getNoreg());
//                    objHR_PGM.put("name", HR_PGM.getName());
//                    objHR_PGM.put("position", HR_PGM.getPositionAbbr());
//                    objHR_PGM.put("class", HR_PGM.getClasS());
//                    objHR_PGM.put("email", HR_PGM.getEmail());
//                    objHR_PGM.put("ttd_hr_pgm", ttdHrPgm);
//                    objHR.put("approver_hr_pgm", objHR_PGM);

                    mainObj.put("hrd", objHR);

                    mainObj.write(response.getWriter());

                } catch (JSONException ex) {
                    LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
                }
            }
        } else {
            try {
                mainObj.put("status", false);
                mainObj.put("message", "You Must Login First.");
                mainObj.write(response.getWriter());
            } catch (JSONException ex) {
                LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
            }
        }

    }

    public List<Approver> getAllApprover(Approver currentUser) {
        List<Approver> listApprover = new ArrayList<Approver>();

        Approver appr = new Approver();

        GetApproverMappingDao dao = new GetApproverMappingDao();
        Approver dhDdhpDhh = new Approver();
        Approver dh = new Approver();
        Approver dphPmSo = new Approver();
        Approver dph = new Approver();
        Approver pm = new Approver();
        Approver so = new Approver();
        Approver sh = new Approver();
        Approver lhGl = new Approver();
        Approver ghTl = new Approver();
        Approver oprTm = new Approver();

        /*jika OPR/TM*/
        LogUtil.info(getClass().getName(), "POSITION_ " + currentUser.getPositionAbbr());
        if ("TM".equalsIgnoreCase(currentUser.getPositionAbbr())) {
            /*cari GH/TL nya*/
            ghTl = dao.getUserApproverSH("TL", currentUser);
            /*GH/TL,LH/GL,SH masih dalam satu SECTION jadi semuanya bisa pake method dao.getUserApproverSH*/
            if (ghTl.getNoreg() != null) {
                listApprover.add(ghTl);
                 LogUtil.info(getClass().getName(), " 1 " + ghTl.getNoreg());
                /*cari LH/GL nya*/
                lhGl = dao.getUserApproverSH("GL", currentUser);
                if (lhGl.getNoreg() != null) {
                    listApprover.add(lhGl);
                    LogUtil.info(getClass().getName(), " 2 " + lhGl.getNoreg());
                    /*cari SH nya*/
                    sh = dao.getUserApproverSH("SH", currentUser);

                    //jika SH dalam satu department tidak ada, maka masuk ke DPH
                    if (sh.getNoreg() == null) {
                         LogUtil.info(getClass().getName(), " 3 " + sh.getNoreg());
                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        if (dphPmSo.getNoreg() != null) {
                             LogUtil.info(getClass().getName(), " 4 " + dphPmSo.getNoreg());
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                  LogUtil.info(getClass().getName(), " 5 " + dhDdhpDhh.getNoreg());
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                            dphPmSo.setIsError(true);
                            dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                            listApprover.add(dphPmSo);

                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                            //cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                  LogUtil.info(getClass().getName(), " 6 " + dhDdhpDhh.getNoreg());
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }
                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                        }

                    } else {
                        //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                        if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                            listApprover.add(dphPmSo);
                              LogUtil.info(getClass().getName(), " 7 " + sh.getNoreg());
                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                  LogUtil.info(getClass().getName(), " 8 " + dhDdhpDhh.getNoreg());
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //jika SH dan DPH nya beda orang maka masuk ke SH
                            listApprover.add(sh);

                            //NOTIC
                            //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                            if (dphPmSo.getNoreg() != null) {
                                listApprover.add(dphPmSo);

                                //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                                if (dhDdhpDhh.getNoreg() != null) {
                                    listApprover.add(dhDdhpDhh);
                                } else {
                                    //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                    dhDdhpDhh.setIsError(true);
                                    dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                    listApprover.add(dhDdhpDhh);
                                }
                            }
                        }
                    }

                } else {
                    //data tidak normal karena tidak ada LH/GL nya
                    lhGl.setIsError(true);
                    lhGl.setMessage("data tidak normal karena tidak ada LH/GL nya");
                    listApprover.add(lhGl);
                      LogUtil.info(getClass().getName(), " GL_TIDAK_ADA " );
                    /*#####LONCAT SATU LEVEL KE - SH#####*/
 /*cari SH nya*/
                    sh = dao.getUserApproverSH("SH", currentUser);

                    //jika SH dalam satu department tidak ada, maka masuk ke DPH
                    if (sh.getNoreg() == null) {

                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        if (dphPmSo.getNoreg() != null) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                            dphPmSo.setIsError(true);
                            dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                            listApprover.add(dphPmSo);

                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                            //cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }
                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                        }

                    } else {
                        //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                        if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //jika SH dan DPH nya beda orang maka masuk ke SH
                            listApprover.add(sh);

                            //NOTIC
                            //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                            if (dphPmSo.getNoreg() != null) {
                                listApprover.add(dphPmSo);

                                //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                                if (dhDdhpDhh.getNoreg() != null) {
                                    listApprover.add(dhDdhpDhh);
                                } else {
                                    //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                    dhDdhpDhh.setIsError(true);
                                    dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                    listApprover.add(dhDdhpDhh);
                                }
                            }
                        }
                    }

                    /*#####LONCAT SATU LEVEL KE - SH#####*/
                }
            } else {
                //data tidak normal karena tidak ada GH/TL nya
                ghTl.setIsError(true);
                ghTl.setMessage("data tidak normal karena tidak ada GH/TL nya");
                listApprover.add(ghTl);

                /*#####LONCAT SATU LEVEL KE - LH/GL#####*/
 /*cari LH/GL nya*/
                lhGl = dao.getUserApproverSH("GL", currentUser);
                if (lhGl.getNoreg() != null) {
                    listApprover.add(lhGl);

                    /*cari SH nya*/
                    sh = dao.getUserApproverSH("SH", currentUser);

                    //jika SH dalam satu department tidak ada, maka masuk ke DPH
                    if (sh.getNoreg() == null) {

                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        if (dphPmSo.getNoreg() != null) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                            dphPmSo.setIsError(true);
                            dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                            listApprover.add(dphPmSo);

                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                            //cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }
                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                        }

                    } else {
                        //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                        if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //jika SH dan DPH nya beda orang maka masuk ke SH
                            listApprover.add(sh);

                            //NOTIC
                            //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                            if (dphPmSo.getNoreg() != null) {
                                listApprover.add(dphPmSo);

                                //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                                if (dhDdhpDhh.getNoreg() != null) {
                                    listApprover.add(dhDdhpDhh);
                                } else {
                                    //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                    dhDdhpDhh.setIsError(true);
                                    dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                    listApprover.add(dhDdhpDhh);
                                }
                            }
                        }
                    }
                } else {
                    //data tidak normal karena tidak ada LH/GL nya
                    lhGl.setIsError(true);
                    lhGl.setMessage("data tidak normal karena tidak ada LH/GL nya");
                    listApprover.add(lhGl);

                    /*#####LONCAT SATU LEVEL KE - SH#####*/
 /*cari SH nya*/
                    sh = dao.getUserApproverSH("SH", currentUser);

                    //jika SH dalam satu department tidak ada, maka masuk ke DPH
                    if (sh.getNoreg() == null) {

                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        if (dphPmSo.getNoreg() != null) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                            dphPmSo.setIsError(true);
                            dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                            listApprover.add(dphPmSo);

                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                            //cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }
                            /*#####LONCAT SATU LEVEL KE - DH#####*/
                        }

                    } else {
                        //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                        dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                        //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                        if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }

                        } else {
                            //jika SH dan DPH nya beda orang maka masuk ke SH
                            listApprover.add(sh);

                            //NOTIC
                            //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                            if (dphPmSo.getNoreg() != null) {
                                listApprover.add(dphPmSo);

                                //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                                if (dhDdhpDhh.getNoreg() != null) {
                                    listApprover.add(dhDdhpDhh);
                                } else {
                                    //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH) 
                                    dhDdhpDhh.setIsError(true);
                                    dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                    listApprover.add(dhDdhpDhh);
                                }
                            }
                        }
                    }
                    /*#####LONCAT SATU LEVEL KE - SH#####*/
                }
                /*#####LONCAT SATU LEVEL KE - LH#####*/
            }
        } //jika GH/TL 
        else if ("TL".equalsIgnoreCase(currentUser.getPositionAbbr())) {

            /*cari LH/GL nya*/
            lhGl = dao.getUserApproverSH("GL", currentUser);
            if (lhGl.getNoreg() != null) {
                listApprover.add(lhGl);

                /*cari SH nya*/
                sh = dao.getUserApproverSH("SH", currentUser);

                //jika SH dalam satu department tidak ada, maka masuk ke DPH
                if (sh.getNoreg() == null) {

                    dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                    if (dphPmSo.getNoreg() != null) {
                        listApprover.add(dphPmSo);

                        //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }

                    } else {
                        //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                        dphPmSo.setIsError(true);
                        dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                        listApprover.add(dphPmSo);

                        /*#####LONCAT SATU LEVEL KE - DH#####*/
                        //cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }
                        /*#####LONCAT SATU LEVEL KE - DH#####*/
                    }

                } else {
                    //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                    dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                    //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                    if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                        listApprover.add(dphPmSo);

                        //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }

                    } else {
                        //jika SH dan DPH nya beda orang maka masuk ke SH
                        listApprover.add(sh);

                        //NOTIC
                        //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                        if (dphPmSo.getNoreg() != null) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }
                        }
                    }
                }
            } else {
                //data tidak normal karena tidak ada LH/GL nya
                lhGl.setIsError(true);
                lhGl.setMessage("data tidak normal karena tidak ada LH/GL nya");
                listApprover.add(lhGl);

                /*#####LONCAT SATU LEVEL KE - SH#####*/
 /*cari SH nya*/
                sh = dao.getUserApproverSH("SH", currentUser);

                //jika SH dalam satu department tidak ada, maka masuk ke DPH
                if (sh.getNoreg() == null) {

                    dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                    if (dphPmSo.getNoreg() != null) {
                        listApprover.add(dphPmSo);

                        //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }

                    } else {
                        //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                        dphPmSo.setIsError(true);
                        dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                        listApprover.add(dphPmSo);

                        /*#####LONCAT SATU LEVEL KE - DH#####*/
                        //cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }
                        /*#####LONCAT SATU LEVEL KE - DH#####*/
                    }

                } else {
                    //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                    dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                    //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                    if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                        listApprover.add(dphPmSo);

                        //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }

                    } else {
                        //jika SH dan DPH nya beda orang maka masuk ke SH
                        listApprover.add(sh);

                        //NOTIC
                        //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                        if (dphPmSo.getNoreg() != null) {
                            listApprover.add(dphPmSo);

                            //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                            if (dhDdhpDhh.getNoreg() != null) {
                                listApprover.add(dhDdhpDhh);
                            } else {
                                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                                dhDdhpDhh.setIsError(true);
                                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                                listApprover.add(dhDdhpDhh);
                            }
                        }
                    }
                }
                /*#####LONCAT SATU LEVEL KE - SH#####*/
            }

        } //jika LH/GL 
        else if ("GL".equalsIgnoreCase(currentUser.getPositionAbbr())) {

            /*cari SH nya*/
            sh = dao.getUserApproverSH("SH", currentUser);

            //jika SH dalam satu department tidak ada, maka masuk ke DPH
            if (sh.getNoreg() == null) {

                dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                if (dphPmSo.getNoreg() != null) {
                    listApprover.add(dphPmSo);

                    //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                    if (dhDdhpDhh.getNoreg() != null) {
                        listApprover.add(dhDdhpDhh);
                    } else {
                        //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh.setIsError(true);
                        dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                        listApprover.add(dhDdhpDhh);
                    }

                } else {
                    //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                    dphPmSo.setIsError(true);
                    dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                    listApprover.add(dphPmSo);

                    /*#####LONCAT SATU LEVEL KE - DH#####*/
                    //cari DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                    if (dhDdhpDhh.getNoreg() != null) {
                        listApprover.add(dhDdhpDhh);
                    } else {
                        //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh.setIsError(true);
                        dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                        listApprover.add(dhDdhpDhh);
                    }
                    /*#####LONCAT SATU LEVEL KE - DH#####*/
                }

            } else {
                //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                    listApprover.add(dphPmSo);

                    //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                    if (dhDdhpDhh.getNoreg() != null) {
                        listApprover.add(dhDdhpDhh);
                    } else {
                        //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh.setIsError(true);
                        dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                        listApprover.add(dhDdhpDhh);
                    }

                } else {
                    //jika SH dan DPH nya beda orang maka masuk ke SH
                    listApprover.add(sh);

                    //NOTIC
                    //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                    if (dphPmSo.getNoreg() != null) {
                        listApprover.add(dphPmSo);

                        //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }
                    }
                }
            }

        } //jika STAFF 
        else if ("STAFF".equalsIgnoreCase(currentUser.getPositionAbbr())) {
            
            LogUtil.info(getClass().getName(), "MASUK VALIDASI STAF");
            lhGl = dao.getUserApproverSH("GL", currentUser);
            if (lhGl.getNoreg() != null) {
                listApprover.add(lhGl);
            }
            /*cari SH nya*/
            sh = dao.getUserApproverSH("SH", currentUser);

            //jika SH dalam satu department tidak ada, maka masuk ke DPH
            if (sh.getNoreg() == null) {

                dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                if (dphPmSo.getNoreg() != null) {
                    listApprover.add(dphPmSo);

                    //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                    if (dhDdhpDhh.getNoreg() != null) {
                        listApprover.add(dhDdhpDhh);
                    } else {
                        //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh.setIsError(true);
                        dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                        listApprover.add(dhDdhpDhh);
                    }

                } else {
                    //data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)
                    dphPmSo.setIsError(true);
                    dphPmSo.setMessage("data tidak normal karena tidak ada DPH atau yang selevel DPH (PM/SO)");
                    listApprover.add(dphPmSo);

                    /*#####LONCAT SATU LEVEL KE - DH#####*/
                    //cari DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                    if (dhDdhpDhh.getNoreg() != null) {
                        listApprover.add(dhDdhpDhh);
                    } else {
                        //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh.setIsError(true);
                        dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                        listApprover.add(dhDdhpDhh);
                    }
                    /*#####LONCAT SATU LEVEL KE - DH#####*/
                }

            } else {
                //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya

                dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

                //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
                if (sh.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                    listApprover.add(sh); //tambahan ca toyota bila SH dan DPHnya sama
                    listApprover.add(dphPmSo);

                    //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                    if (dhDdhpDhh.getNoreg() != null) {
                        listApprover.add(dhDdhpDhh);
                    } else {
                        //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh.setIsError(true);
                        dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                        listApprover.add(dhDdhpDhh);
                    }

                } else {
                    //jika SH dan DPH nya beda orang maka masuk ke SH
                    listApprover.add(sh);

                    //NOTIC
                    //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                    if (dphPmSo.getNoreg() != null) {
                        listApprover.add(dphPmSo);

                        //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                        if (dhDdhpDhh.getNoreg() != null) {
                            listApprover.add(dhDdhpDhh);
                        } else {
                            //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                            dhDdhpDhh.setIsError(true);
                            dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                            listApprover.add(dhDdhpDhh);
                        }
                    }
                }
            }

        } else if ("SH".equalsIgnoreCase(currentUser.getPositionAbbr())) {

            //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya
            dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);
            LogUtil.info(getClass().getName(), "MASUK VALIDASI SH");
            lhGl = dao.getUserApproverSH("GL", currentUser);
            if (lhGl.getNoreg() != null) {
                 listApprover.add(lhGl);
            }
            //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
            if (currentUser.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                listApprover.add(dphPmSo);

                //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                if (dhDdhpDhh.getNoreg() != null) {
                    listApprover.add(dhDdhpDhh);
                } else {
                    //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh.setIsError(true);
                    dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                    listApprover.add(dhDdhpDhh);
                }
            } else {

                //NOTIC
                //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                if (dphPmSo.getNoreg() != null) {
                    listApprover.add(dphPmSo);

                    //jika sudah dapat yang selevel DPH(PM/SO) cari DH nya atau selevel DH(DDH-P/DDH)
                    dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
                    if (dhDdhpDhh.getNoreg() != null) {
                        listApprover.add(dhDdhpDhh);
                    } else {
                        //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                        dhDdhpDhh.setIsError(true);
                        dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                        listApprover.add(dhDdhpDhh);
                    }
                }
            }
        } else if ("DPH".equalsIgnoreCase(currentUser.getPositionAbbr()) || "SO".equalsIgnoreCase(currentUser.getPositionAbbr()) || "PM".equalsIgnoreCase(currentUser.getPositionAbbr())) {

            //cari DH nya atau selevel DH(DDH-P/DDH)
            dhDdhpDhh = this.getApproverOneLevelDhDdhpDdh(currentUser);
            if (dhDdhpDhh.getNoreg() != null) {
                listApprover.add(dhDdhpDhh);
            } else {
                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                dhDdhpDhh.setIsError(true);
                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                listApprover.add(dhDdhpDhh);
            }
        }

        return listApprover;
    }

    public Approver getApproverOneLevelDphPmSo(Approver currentUser) {
        Approver approval = new Approver();
        Approver dph = new Approver();
        Approver pm = new Approver();
        Approver so = new Approver();

        dph = dao.getUserApproverDPH("DPH", currentUser);

        //jika DPH dalam satu department tidak ada, maka cari PM
        if (dph.getNoreg() == null) {

            pm = dao.getUserApproverDPH("PM", currentUser);
            //jika PM dalam satu department tidak ada, maka cari SO
            if (pm.getNoreg() == null) {

                //jika PM dalam satu department tidak ada, maka cari SO
                so = dao.getUserApproverDPH("SO", currentUser);
                if (so.getNoreg() == null) {
                    LogUtil.info(this.getClass().getName(), "Data Struktur Organisasi Tidak Normal DPH/PM/SO : " + currentUser.getNoreg() + " - " + currentUser.getName() + " - " + currentUser.getDivName());
                } else {
                    approval = so;
                }

            } else {
                //tapi jika PM ada tambahkan PM ke List Approver
                approval = pm;

            }

        } else {
            //tapi jika DPH ada tambahakan DPH ke List Approver
            approval = dph;
        }

        return approval;
    }

    public Approver getApproverOneLevelDhDdhpDdh(Approver currentUser) {
        Approver approval = new Approver();
        Approver dh = new Approver();
        Approver ddhP = new Approver();
        Approver ddh = new Approver();
        Approver dchief = new Approver();

        dh = dao.getUserApproverDH("DH", currentUser);

        if (dh.getNoreg() == null) {

            //jika DH dalam satu department tidak ada, maka cari DDH-P
            ddhP = dao.getUserApproverDH("DDH-P", currentUser);
            if (ddhP.getNoreg() == null) {

                //jika DDH-P dalam satu department tidak ada, maka cari DDH
                ddh = dao.getUserApproverDH("DDH", currentUser);
                if (ddh.getNoreg() == null) {
                    dchief = dao.getUserApproverDH("DCHIEF", currentUser);
                    
                    if (dchief.getNoreg() == null) {
                        LogUtil.info(this.getClass().getName(), "Data Struktur Organisasi Tidak Normal DH/DDH-P/DDH/DCHIEF : " + currentUser.getNoreg() + " - " + currentUser.getName() + " - " + currentUser.getDivName());
                    } else {
                        //tapi jika DCHIEF ada tambahkan DCHIEF ke List Approver
                        approval = dchief;
                    }
                    
                } else {
                    //tapi jika DDH ada tambahkan DDH ke List Approver
                    approval = ddh;
                }

            } else {
                //tapi jika DDH-P ada tambahkan DDH-P ke List Approver
                approval = ddhP;

            }

        } else {
            //tapi jika DH ada tambahakan DH ke List Approver
            approval = dh;
        }

        return approval;
    }

}
