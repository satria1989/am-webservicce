/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.itasoft.tmmin.eform.am.plugin;

import id.co.itasoft.tmmin.eform.am.plugin.dao.GetApproverMappingDao;
import id.co.itasoft.tmmin.eform.am.plugin.model.Approver;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.datalist.model.DataList;
import org.joget.apps.datalist.model.DataListCollection;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginWebSupport;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.WorkflowProcess;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.WorkflowManager;
import org.joget.workflow.model.service.WorkflowUserManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.joget.commons.util.UuidGenerator;
import org.springframework.context.ApplicationContext;

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
public class DelegateAllJobs extends Element implements PluginWebSupport {

    public static String pluginName = "TMMIN - AM - Delegate All Jobs ";

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

    GetApproverMappingDao dao = new GetApproverMappingDao();

    public JSONObject getJSONObjectApproverMapping(String noreg, String currentUserName) {

        JSONObject mainObj = new JSONObject();

        /*Cek Hanya User Yang Sudah Login Bisa Mengakses Ini*/
        if (!currentUserName.isEmpty()) {

            Approver currentUser = new Approver();

            currentUser = dao.getDetailCurrentUserByUsername(currentUserName);

            Approver employee = new Approver();

            employee = dao.getDetailEmployeeByNoreg(noreg);

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

                    String str_dob = employee.getDateOfBirth();
                    LocalDate dob = LocalDate.parse(str_dob);
                    objEmployee.put("age", "" + LoadDataEmployee.calculateAge(dob));

                    mainObj.put("employee", objEmployee);

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
                    Approver HR_ER_SH = new Approver();
                    HR_ER_SH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_ER_SH");

                    /*JSON Response HR_ER_SH*/
                    JSONObject objHR_ER_SH = new JSONObject();
                    objHR_ER_SH.put("username", HR_ER_SH.getUsername());
                    objHR_ER_SH.put("noreg", HR_ER_SH.getNoreg());
                    objHR_ER_SH.put("name", HR_ER_SH.getName());
                    objHR_ER_SH.put("position", HR_ER_SH.getPositionAbbr());
                    objHR_ER_SH.put("class", HR_ER_SH.getClasS());
                    objHR_ER_SH.put("email", HR_ER_SH.getEmail());
                    objHR.put("approver_hr_er_sh", objHR_ER_SH);

                    /*Dapetin APPROVER HR_PTM_SH*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
                    Approver HR_PTM_SH = new Approver();
                    HR_PTM_SH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_PTM_SH");

                    /*JSON Response HR_PTM_SH*/
                    JSONObject objHR_PTM_SH = new JSONObject();
                    objHR_PTM_SH.put("username", HR_PTM_SH.getUsername());
                    objHR_PTM_SH.put("noreg", HR_PTM_SH.getNoreg());
                    objHR_PTM_SH.put("name", HR_PTM_SH.getName());
                    objHR_PTM_SH.put("position", HR_PTM_SH.getPositionAbbr());
                    objHR_PTM_SH.put("class", HR_PTM_SH.getClasS());
                    objHR_PTM_SH.put("email", HR_PTM_SH.getEmail());
                    objHR.put("approver_hr_ptm_sh", objHR_PTM_SH);

                    /*Dapetin APPROVER HR_PTM_DPH*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
                    Approver HR_PTM_DPH = new Approver();
                    HR_PTM_DPH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_PTM_DPH");

                    /*JSON Response HR_PTM_DPH*/
                    JSONObject objHR_PTM_DPH = new JSONObject();
                    objHR_PTM_DPH.put("username", HR_PTM_DPH.getUsername());
                    objHR_PTM_DPH.put("noreg", HR_PTM_DPH.getNoreg());
                    objHR_PTM_DPH.put("name", HR_PTM_DPH.getName());
                    objHR_PTM_DPH.put("position", HR_PTM_DPH.getPositionAbbr());
                    objHR_PTM_DPH.put("class", HR_PTM_DPH.getClasS());
                    objHR_PTM_DPH.put("email", HR_PTM_DPH.getEmail());
                    objHR.put("approver_hr_ptm_dph", objHR_PTM_DPH);

                    /*Dapetin APPROVER HR_ER_DPH*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
                    Approver HR_ER_DPH = new Approver();
                    HR_ER_DPH = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_ER_DPH");

                    /*JSON Response HR_ER_DPH*/
                    JSONObject objHR_ER_DPH = new JSONObject();
                    objHR_ER_DPH.put("username", HR_ER_DPH.getUsername());
                    objHR_ER_DPH.put("noreg", HR_ER_DPH.getNoreg());
                    objHR_ER_DPH.put("name", HR_ER_DPH.getName());
                    objHR_ER_DPH.put("position", HR_ER_DPH.getPositionAbbr());
                    objHR_ER_DPH.put("class", HR_ER_DPH.getClasS());
                    objHR_ER_DPH.put("email", HR_ER_DPH.getEmail());
                    objHR.put("approver_hr_er_dph", objHR_ER_DPH);

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
                    Approver HR_REM = new Approver();
                    HR_REM = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_REM");

                    /*JSON Response HR_DH*/
                    JSONObject objHR_REM = new JSONObject();
                    objHR_REM.put("username", HR_REM.getUsername());
                    objHR_REM.put("noreg", HR_REM.getNoreg());
                    objHR_REM.put("name", HR_REM.getName());
                    objHR_REM.put("position", HR_REM.getPositionAbbr());
                    objHR_REM.put("class", HR_REM.getClasS());
                    objHR_REM.put("email", HR_REM.getEmail());
                    objHR.put("approver_hr_rem", objHR_REM);

                    /*Dapetin APPROVER HR_ER_STAFF*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
                    Approver HR_ER_STAFF = new Approver();
                    HR_ER_STAFF = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_ER_STAFF");

                    /*JSON Response HR_ER_STAFF*/
                    JSONObject objHR_ER_STAFF = new JSONObject();
                    objHR_ER_STAFF.put("username", HR_ER_STAFF.getUsername());
                    objHR_ER_STAFF.put("noreg", HR_ER_STAFF.getNoreg());
                    objHR_ER_STAFF.put("name", HR_ER_STAFF.getName());
                    objHR_ER_STAFF.put("position", HR_ER_STAFF.getPositionAbbr());
                    objHR_ER_STAFF.put("class", HR_ER_STAFF.getClasS());
                    objHR_ER_STAFF.put("email", HR_ER_STAFF.getEmail());
                    objHR.put("approver_hr_er_staff", objHR_ER_STAFF);

                    /*Dapetin APPROVER HR_PGM*/
 /*
                    Section    : 
                    Department : 
                    Div        : Human Resources Div.
                     */
                    Approver HR_PGM = new Approver();
                    HR_PGM = dao.getEmployeeFromMappingMaster("USER_MAPPING_APPROVAL", "HR_PGM");

                    String ttdHrPgm = "";
                    if (!"".equalsIgnoreCase(HR_PGM.getNoreg())) {
                        ttdHrPgm = dao.getTtd(HR_PGM.getNoreg());
                    }
                    /*JSON Response HR_PGM*/
                    JSONObject objHR_PGM = new JSONObject();
                    objHR_PGM.put("username", HR_PGM.getUsername());
                    objHR_PGM.put("noreg", HR_PGM.getNoreg());
                    objHR_PGM.put("name", HR_PGM.getName());
                    objHR_PGM.put("position", HR_PGM.getPositionAbbr());
                    objHR_PGM.put("class", HR_PGM.getClasS());
                    objHR_PGM.put("email", HR_PGM.getEmail());
                    objHR_PGM.put("ttd_hr_pgm", ttdHrPgm);
                    objHR.put("approver_hr_pgm", objHR_PGM);

                    mainObj.put("hrd", objHR);

                } catch (JSONException ex) {
                    LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
                }
            }
        } else {
            try {
                mainObj.put("status", false);
                mainObj.put("message", "You Must Login First.");
            } catch (JSONException ex) {
                LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
            }
        }
        return mainObj;
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
        if ("TM".equalsIgnoreCase(currentUser.getPositionAbbr())) {
            /*cari GH/TL nya*/
            ghTl = dao.getUserApproverSH("TL", currentUser);
            /*GH/TL,LH/GL,SH masih dalam satu SECTION jadi semuanya bisa pake method dao.getUserApproverSH*/
            if (ghTl.getNoreg() != null) {
                listApprover.add(ghTl);

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
                                //hard code
//                                Approver hc = new Approver();
//                                hc.setNoreg("87802213");
//                                hc.setName("DASWAR BASRI");
//                                hc.setPositionAbbr("STAFF");
//                                hc.setClasS("8 AM");
//                                hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                                hc.setUsername("daswar.basri");
//                                listApprover.add(hc);
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
                                //hard code
//                                Approver hc = new Approver();
//                                hc.setNoreg("87802213");
//                                hc.setName("DASWAR BASRI");
//                                hc.setPositionAbbr("STAFF");
//                                hc.setClasS("8 AM");
//                                hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                                hc.setUsername("daswar.basri");
//                                listApprover.add(hc);
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
//                                    Approver hc = new Approver();
//                                    hc.setNoreg("87802213");
//                                    hc.setName("DASWAR BASRI");
//                                    hc.setPositionAbbr("STAFF");
//                                    hc.setClasS("8 AM");
//                                    hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                                    hc.setUsername("daswar.basri");
//                                    listApprover.add(hc);
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
//                            Approver hc = new Approver();
//                            hc.setNoreg("87802213");
//                            hc.setName("DASWAR BASRI");
//                            hc.setPositionAbbr("STAFF");
//                            hc.setClasS("8 AM");
//                            hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                            hc.setUsername("daswar.basri");
//                            listApprover.add(hc);
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
//                            Approver hc = new Approver();
//                            hc.setNoreg("87802213");
//                            hc.setName("DASWAR BASRI");
//                            hc.setPositionAbbr("STAFF");
//                            hc.setClasS("8 AM");
//                            hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                            hc.setUsername("daswar.basri");
//                            listApprover.add(hc);
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
//                            Approver hc = new Approver();
//                            hc.setNoreg("87802213");
//                            hc.setName("DASWAR BASRI");
//                            hc.setPositionAbbr("STAFF");
//                            hc.setClasS("8 AM");
//                            hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                            hc.setUsername("daswar.basri");
//                            listApprover.add(hc);
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
//                                Approver hc = new Approver();
//                                hc.setNoreg("87802213");
//                                hc.setName("DASWAR BASRI");
//                                hc.setPositionAbbr("STAFF");
//                                hc.setClasS("8 AM");
//                                hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                                hc.setUsername("daswar.basri");
//                                listApprover.add(hc);
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
//                            Approver hc = new Approver();
//                            hc.setNoreg("87802213");
//                            hc.setName("DASWAR BASRI");
//                            hc.setPositionAbbr("STAFF");
//                            hc.setClasS("8 AM");
//                            hc.setEmail("windy.kusuma@itasoft.co.id,tarkiman@itasoft.co.id,novi.fibriani@itasoft.co.id,erick.ricky@itasoft.co.id");
//                            hc.setUsername("daswar.basri");
//                            listApprover.add(hc);
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

        } else if ("SH".equalsIgnoreCase(currentUser.getPositionAbbr())) {

            //jika SH nya ada, maka cek DPH atau yang selevel DPH (PM/SO) nya
            dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

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

                //modify 20221021 : jika sudah dapat DH nya atau selevel DH(DDH-P/DDH), cari SH nya
                sh = this.getApproverOneLevelSH(currentUser);
                if (sh.getNoreg() != null) {
                    listApprover.add(sh);
                } else {
                    //data tidak normal karena tidak ada SH nya
                    sh.setIsError(true);
                    sh.setMessage("data tidak normal karena tidak ada SH nya");
                    listApprover.add(sh);
                }
            } else {
                //data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)
                dhDdhpDhh.setIsError(true);
                dhDdhpDhh.setMessage("data tidak normal karena tidak ada DH nya atau selevel DH(DDH-P/DDH)");
                listApprover.add(dhDdhpDhh);
            }
        } //modify 20221021 : cari approval untuk DH 
        else if ("DH".equalsIgnoreCase(currentUser.getPositionAbbr()) || "DDH-P".equalsIgnoreCase(currentUser.getPositionAbbr()) || "DDH".equalsIgnoreCase(currentUser.getPositionAbbr())) {
            //jika DH nya atau selevel DH(DDH-P/DDH) ada, maka cek DPH atau yang selevel DPH (PM/SO) nya
            dphPmSo = this.getApproverOneLevelDphPmSo(currentUser);

            //jika SH dan DPH atau yang selevel DPH (PM/SO) memiliki noreg yang sama (orang yang sama) maka masuk ke DPH atau yang selevel DPH (PM/SO)
            if (currentUser.getNoreg().equalsIgnoreCase(dphPmSo.getNoreg())) {
                listApprover.add(dphPmSo);

                //jika sudah dapat yang selevel DPH(PM/SO), cari SH nya 
                //modify 20221021 : cari SH nya
                sh = this.getApproverOneLevelSH(currentUser);
                if (sh.getNoreg() != null) {
                    listApprover.add(sh);
                } else {
                    //data tidak normal karena tidak ada SH nya
                    sh.setIsError(true);
                    sh.setMessage("data tidak normal karena tidak ada SH nya");
                    listApprover.add(sh);
                }
            } else {

                //NOTIC
                //jika sudah masuk sini skalian cari DPH nya karena SH nya beda orang
                if (dphPmSo.getNoreg() != null) {
                    listApprover.add(dphPmSo);

                    //modify 20221021 : jika sudah dapat yang selevel DPH(PM/SO), cari SH nya
                    sh = this.getApproverOneLevelSH(currentUser);
                    if (sh.getNoreg() != null) {
                        listApprover.add(sh);
                    } else {
                        //data tidak normal karena tidak ada SH nya
                        sh.setIsError(true);
                        sh.setMessage("data tidak normal karena tidak ada SH nya");
                        listApprover.add(sh);
                    }
                }
            }
        }

        return listApprover;
    }

    public Approver getApproverOneLevelSH(Approver currentUser) {
        Approver approval = new Approver();
        Approver sh = new Approver();

        sh = dao.getUserApproverDPH("SH", currentUser);

        //jika DPH dalam satu department tidak ada, maka cari PM
        if (sh.getNoreg() == null) {
            LogUtil.info(this.getClass().getName(), "Data Struktur Organisasi Tidak Normal SH : " + currentUser.getNoreg() + " - " + currentUser.getName() + " - " + currentUser.getDivName());
        } else {
            //tapi jika SH ada tambahakan SH ke List Approver
            approval = sh;
        }

        return approval;
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

        dh = dao.getUserApproverDH("DH", currentUser);

        if (dh.getNoreg() == null) {

            //jika DH dalam satu department tidak ada, maka cari DDH-P
            ddhP = dao.getUserApproverDH("DDH-P", currentUser);
            if (ddhP.getNoreg() == null) {

                //jika DDH-P dalam satu department tidak ada, maka cari DDH
                ddh = dao.getUserApproverDH("DDH", currentUser);
                if (ddh.getNoreg() == null) {
                    LogUtil.info(this.getClass().getName(), "Data Struktur Organisasi Tidak Normal DH/DDH-P/DDH : " + currentUser.getNoreg() + " - " + currentUser.getName() + " - " + currentUser.getDivName());
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

    public void bulkDelegateJobs(WorkflowManager workflowManager, String username, JSONObject mainObj) {

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        StartDelegateCAJobs ex = new StartDelegateCAJobs(this, workflowManager, username, ds);
        Thread t1 = new Thread(ex);
        t1.start();
        LogUtil.info(getClass().getName(), "################################");
    }

    public class StartDelegateCAJobs implements Runnable {

        WorkflowManager workflowManager;
        String username;
        DataSource ds;
        DelegateAllJobs parentThread;

        public StartDelegateCAJobs(DelegateAllJobs parentThread, WorkflowManager workflowManager, String username, DataSource ds) {
            this.workflowManager = workflowManager;
            this.username = username;
            this.ds = ds;
            this.parentThread = parentThread;
        }
        
        public String getReceipientDPH(String position,  String deptName){
            StringBuilder query = new StringBuilder();
            query
                    .append("SELECT ")
                    .append("a.c_username ")
                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                    .append("WHERE a.c_position_abbr=? ")
                    .append("and a.c_dept_name=?");
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            String receipient = "";
            
            try {
                Connection con = ds.getConnection();
                ps = con.prepareStatement(query.toString());
                ps.setString(1, position);
                ps.setString(2, deptName);
                rs = ps.executeQuery();
                boolean error = false;

                //try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receipient = rs.getString("c_username");
                } else {
                    LogUtil.info(this.getClass().getName(), "Error : username not found");
                }
            } catch (SQLException e) {
                LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return receipient;
        }
        
        /*
        * Di tambahkan code untuk user yg rangkap jabatan karena
          Error : The requested operation is not supported on forward only result sets.
          by satria.
        */
        public int countDouble(String position, String userName) {
            StringBuilder query = new StringBuilder();
            query
                    .append("SELECT ")
                    .append("count(*) as jumlah ")
                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                    .append("WHERE a.c_position_abbr=? ")
                    .append("and a.c_username=?");

            PreparedStatement ps = null;
            ResultSet rs = null;
            int receipient = 0;

            try {
                Connection con = ds.getConnection();
                ps = con.prepareStatement(query.toString());
                ps.setString(1, position);
                ps.setString(2, userName);
                rs = ps.executeQuery();
                boolean error = false;

                //try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receipient = rs.getInt("jumlah");
                    LogUtil.info(getClass().getName(), " isRangkapJabatan : " + receipient);
                } else {
                    LogUtil.info(this.getClass().getName(), "Error : username not found");
                }
            } catch (SQLException e) {
                LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return receipient;
        }
        
        public String getReceipientSH(String position,  String sectName){
            StringBuilder query = new StringBuilder();
            query
                    .append("SELECT ")
                    .append("a.c_username ")
                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                    .append("WHERE a.c_position_abbr=? ")
                    .append("and a.c_section_name=?");
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            String receipient = "";
            
            try {
                Connection con = ds.getConnection();
                ps = con.prepareStatement(query.toString());
                ps.setString(1, position);
                ps.setString(2, sectName);
                rs = ps.executeQuery();
                boolean error = false;

                //try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receipient = rs.getString("c_username");
                } else {
                    LogUtil.info(this.getClass().getName(), "Error : username not found");
                }
            } catch (SQLException e) {
                LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return receipient;
        }
        
        public String getReceipientGL(String position,  String sectName){
            StringBuilder query = new StringBuilder();
            query
                    .append("SELECT ")
                    .append("a.c_username ")
                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                    .append("WHERE a.c_position_abbr=? ")
                    .append("and a.c_section_name=?");
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            String receipient = "";
            
            try {
                Connection con = ds.getConnection();
                ps = con.prepareStatement(query.toString());
                ps.setString(1, position);
                ps.setString(2, sectName);
                rs = ps.executeQuery();
                boolean error = false;

                //try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receipient = rs.getString("c_username");
                } else {
                    LogUtil.info(this.getClass().getName(), "Error : username not found");
                }
            } catch (SQLException e) {
                LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return receipient;
        }
        
        public void sendDelegateEmail(String div, String dept, String position){
//            StringBuilder query = new StringBuilder();
//            query
//                    .append("SELECT ")
//                    .append("a.c_div_name, a.c_position_abbr, a.c_dept_name ")
//                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
//                    .append("WHERE a.c_username=? and a.c_position_percent='100'");
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            LogUtil.info(this.getClass().getName(), "position->"+position);
            LogUtil.info(this.getClass().getName(), "div->"+div);
            LogUtil.info(this.getClass().getName(), "dept->"+dept);
            
            try {
                Connection con = ds.getConnection();
//                ps = con.prepareStatement(query.toString());
//                ps.setString(1, username);
//                rs = ps.executeQuery();
                boolean error = false;

                //try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    String position = rs.getString("c_position_abbr");
//                    String resultDivName = rs.getString("c_div_name");
//                    String resultDeptName = rs.getString("c_dept_name");
                    String deptName, sectionName, query, receipient;
                    
                    if ("DH".equalsIgnoreCase(position) || "DDH-P".equalsIgnoreCase(position) || "DDH".equalsIgnoreCase(position) || "DCHIEF".equalsIgnoreCase(position)) {
                        query = "SELECT DISTINCT c_dept_name "
                            + "FROM app_fd_employee "
                            + "WHERE c_div_name=? "
                            + "and c_ca_year is null "
                            + "and c_ca_status='Delegate to DPH' "
                            + "and (c_dept_name is not null and c_dept_name != 'NULL')";
                        
                        ps = con.prepareStatement(query);
                        ps.setString(1, div);
                        rs = ps.executeQuery();
                        WorkflowProcessResult results;
                        
                        while(rs.next()){
                            deptName = rs.getString("c_dept_name");
                            LogUtil.info(this.getClass().getName(), "LINE 1628 DN->"+deptName);
                            receipient = getReceipientDPH("DPH", deptName);
                            if("".equalsIgnoreCase(receipient)){
                                receipient = getReceipientDPH("PM", deptName);
                                if("".equalsIgnoreCase(receipient)){
                                    receipient = getReceipientDPH("SO", deptName);
                                    if("".equalsIgnoreCase(receipient)){
                                        LogUtil.info(this.getClass().getName(), "DPH Tidak Ditemukan");
                                    }
                                }
                            }
                            
                            if(!"".equalsIgnoreCase(receipient)){
                                ApplicationContext ac = AppUtil.getApplicationContext();
                                WorkflowManager workflowManager = (WorkflowManager) ac.getBean("workflowManager");
                                Map<String, String> variables = new HashMap<String, String>();
                                variables = new HashMap();
                                variables.put("requester", receipient);
                                results = workflowManager.processStart("appraisalManagement:latest:sendDelegateEmailDPH", variables);
                                results.getProcess().toString();
                                LogUtil.info(this.getClass().getName(), "LINE 1648 Results->"+results.getProcess().toString());
                            }
                            LogUtil.info(this.getClass().getName(), "LINE 1650 Receipient DN->"+receipient);
                        }
                    } else if ("DPH".equalsIgnoreCase(position) || "SO".equalsIgnoreCase(position) || "PM".equalsIgnoreCase(position)) {
                        query = "SELECT DISTINCT c_section_name, c_dept_name "
                            + "FROM app_fd_employee "
                            + "WHERE c_dept_name=? and c_div_name=? "
                            + "and c_ca_year is null "
                            + "and c_ca_status='Delegate to SH' "
                            + "and (c_section_name is not null and c_section_name != 'NULL')";
                        
                        ps = con.prepareStatement(query);
                        ps.setString(1, dept);
                        ps.setString(2, div);
                        rs = ps.executeQuery();
                        WorkflowProcessResult results;
                        while(rs.next()){
                            sectionName = rs.getString("c_section_name");
                            LogUtil.info(this.getClass().getName(), "LINE 1666 SN->"+sectionName);
                            receipient = getReceipientSH("SH", sectionName);
                            if("".equalsIgnoreCase(receipient)){
                                LogUtil.info(this.getClass().getName(), "SH Tidak Ditemukan");
                            } else{
                                ApplicationContext ac = AppUtil.getApplicationContext();
                                WorkflowManager workflowManager = (WorkflowManager) ac.getBean("workflowManager");
                                Map<String, String> variables = new HashMap<String, String>();
                                variables = new HashMap();
                                variables.put("requester", receipient);
                                results = workflowManager.processStart("appraisalManagement:latest:sendDelegateEmailSH", variables);
                                results.getProcess().toString();
                                LogUtil.info(this.getClass().getName(), "LINE 1678 Results->"+results.getProcess().toString());
                            }
                            LogUtil.info(this.getClass().getName(), "LINE 1680 Receipient SN->"+receipient);
                        }
                    }else if ("GL".equalsIgnoreCase(position)) {
                        query = "SELECT DISTINCT c_section_name, c_dept_name "
                            + "FROM app_fd_employee "
                            + "WHERE c_dept_name=? and c_div_name=? "
                            + "and c_ca_year is null "
                            + "and c_ca_status='Delegate to GL' "
                            + "and (c_section_name is not null and c_section_name != 'NULL')";
                        
                        ps = con.prepareStatement(query);
                        ps.setString(1, dept);
                        ps.setString(2, div);
                        rs = ps.executeQuery();
                        WorkflowProcessResult results;
                        while(rs.next()){
                            sectionName = rs.getString("c_section_name");
                            LogUtil.info(this.getClass().getName(), "LINE 1666 SN->"+sectionName);
                            receipient = getReceipientSH("GL", sectionName);
                            if("".equalsIgnoreCase(receipient)){
                                LogUtil.info(this.getClass().getName(), "GL Tidak Ditemukan");
                            } else{
                                ApplicationContext ac = AppUtil.getApplicationContext();
                                WorkflowManager workflowManager = (WorkflowManager) ac.getBean("workflowManager");
                                Map<String, String> variables = new HashMap<String, String>();
                                variables = new HashMap();
                                variables.put("requester", receipient);
                                results = workflowManager.processStart("appraisalManagement:latest:sendDelegateEmailGL", variables);
                                results.getProcess().toString();
                                LogUtil.info(this.getClass().getName(), "LINE 1678 Results->"+results.getProcess().toString());
                            }
                            LogUtil.info(this.getClass().getName(), "LINE 1680 Receipient SN->"+receipient);
                        }
                    }
//                } else {
//                    LogUtil.info(this.getClass().getName(), "Error : username not found");
//                }

            } catch (SQLException e) {
                LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
//                if (rs2 != null) {
//                    try {
//                        rs2.close();
//                    } catch (SQLException ex) {
//                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//
//                if (ps2 != null) {
//                    try {
//                        ps2.close();
//                    } catch (SQLException ex) {
//                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }

                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        @Override
        public void run() {
            StringBuilder query = new StringBuilder();
            query
                    .append("SELECT ")
                    .append("a.c_div_name, a.c_position_abbr, a.c_dept_name ")
                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                    .append("WHERE a.c_username=? and a.c_position_percent='100'");

            PreparedStatement ps = null, ps2 = null, ps3 =null, ps4 = null;
            ResultSet rs = null, rs2 = null, rs3 = null, rs4 = null;

            try {
                Connection con = ds.getConnection();
                ps = con.prepareStatement(query.toString());
                ps.setString(1, username);
                rs = ps.executeQuery();
                boolean error = false;

                //try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

//                    String query2 = "UPDATE app_fd_employee "
//                            + "SET c_ca_status=?, dateModified=GETDATE() "
//                            + "WHERE c_div_name=? "
//                            + "and c_status!='Expatriate' "
//                            + "and c_position_abbr != 'ICT' "
//                            + "and c_position_abbr != 'DPH' and c_position_abbr != 'SO' and c_position_abbr != 'PM' "
//                            + "and c_position_abbr != 'DH' and c_position_abbr != 'DDH-P' and c_position_abbr != 'DDH' and c_position_abbr != 'DCHIEF' ";
//
//                    String position = rs.getString("c_position_abbr");
//                    String resultDivName = rs.getString("c_div_name");
//                    String resultDeptName = rs.getString("c_dept_name");
//                    String resultSectionName = "";
//
//                    LogUtil.info(this.getClass().getName(), "position= " + position);
//                    LogUtil.info(this.getClass().getName(), "div_name= " + resultDivName);
//                    LogUtil.info(this.getClass().getName(), "dept_name= " + resultDeptName);
//
//                    String c_ca_status = "";
//                    if ("DH".equalsIgnoreCase(position) || "DDH-P".equalsIgnoreCase(position) || "DDH".equalsIgnoreCase(position) || "DCHIEF".equalsIgnoreCase(position)) {
//                        c_ca_status = "Delegate to DPH";
//                        query2 += "and c_ca_status is null";
//                    } else if ("DPH".equalsIgnoreCase(position) || "SO".equalsIgnoreCase(position) || "PM".equalsIgnoreCase(position)) {
//                        c_ca_status = "Delegate to SH";
//                        query2 += "and c_ca_status='Delegate to DPH' ";
//                        query2 += "and c_position_abbr != 'SH' "; 
//                        query2 += "and c_dept_name =?";
//                        
//                        StringBuilder query3 = new StringBuilder();
//                        // Cari Rangkap Jabatan DPH dan SH
//                        query3
//                                .append("SELECT ")
//                                .append("a.c_div_name, a.c_position_abbr, a.c_dept_name, a.c_section_name ")
//                                .append("FROM app_fd_employee a WITH(NOLOCK) ")
//                                .append("WHERE a.c_username=? and a.c_position_abbr='SH' ");
//                        ps3 = con.prepareStatement(query3.toString());
//                        ps3.setString(1, username);
//                        rs3 = ps3.executeQuery();
//                        
//                        // Jika Tidak Rangkap Jabatan, resultSectionName diset kosong
//                        // Jika Double Jabatan, resultSectionName diset c_section_name. Lalu ditambahkahkan filter c_section_name di query2
//                        if(!rs3.isBeforeFirst() && rs3.getRow() == 0){
//                            resultSectionName = "";
//                        } else{
//                            if (rs3.next()) {
//                                resultSectionName=rs3.getString("c_section_name");
//                            }
//                            query2 += " and c_section_name !=?";
//                        }
//                        LogUtil.info(this.getClass().getName(), "section_name= " + resultSectionName);
//                        
//                    }
//                    
//                    ps2 = con.prepareStatement(query2);
//
//                    ps2.setString(1, c_ca_status);
//                    ps2.setString(2, resultDivName);
//                    if("DPH".equalsIgnoreCase(position) || "SO".equalsIgnoreCase(position) || "PM".equalsIgnoreCase(position)){
//                        ps2.setString(3, resultDeptName);
//                        // Jika resultSectionName tidak sama dengan null, resultSectionName diset sebagai parameter ke-4 di query2
//                        if(!resultSectionName.equals("")){
//                            ps2.setString(4, resultSectionName);
//                        }
//                    }
//                    ps2.executeUpdate();
//                    sendDelegateEmail();
                    String position = rs.getString("c_position_abbr");
                    String resultDivName = rs.getString("c_div_name");
                    String resultDeptName = rs.getString("c_dept_name");
                    String resultSectionName = "";

                    LogUtil.info(this.getClass().getName(), "position= " + position);
                    LogUtil.info(this.getClass().getName(), "div_name= " + resultDivName);
                    LogUtil.info(this.getClass().getName(), "dept_name= " + resultDeptName);

                    String c_ca_status = "";
                    if ("DH".equalsIgnoreCase(position) || "DDH-P".equalsIgnoreCase(position) || "DDH".equalsIgnoreCase(position) || "DCHIEF".equalsIgnoreCase(position)) {
                        String query2 = "SELECT "
                                + "a.c_div_name "
                                + "FROM app_fd_employee a WITH(NOLOCK) "
                                + "WHERE a.c_username=? "
                                + "and (a.c_position_abbr = 'DH' or a.c_position_abbr = 'DDH-P' or a.c_position_abbr = 'DDH' or a.c_position_abbr = 'DCHIEF')";
                        ps2 = con.prepareStatement(query2);
                        ps2.setString(1, username);
                        rs2 = ps2.executeQuery();
                        
                        String div;
                        
                        while (rs2.next()) {
                            c_ca_status = "Delegate to DPH";
                            div = rs.getString("c_div_name");
                            String query3 = "UPDATE app_fd_employee "
                                    + "SET c_ca_status=?, dateModified=GETDATE() "
                                    + "WHERE c_div_name=? "
                                    + "and c_status!='Expatriate' "
                                    + "and c_position_abbr != 'ICT' "
                                    + "and c_position_abbr != 'DPH' and c_position_abbr != 'SO' and c_position_abbr != 'PM' "
                                    + "and c_position_abbr != 'DH' and c_position_abbr != 'DDH-P' and c_position_abbr != 'DDH' and c_position_abbr != 'DCHIEF' "
                                    + "and c_ca_status is null";

                            ps3 = con.prepareStatement(query3);
                            ps3.setString(1, c_ca_status);
                            ps3.setString(2, div);
                            ps3.executeUpdate();
                            sendDelegateEmail(div, "", position);
                        }
                    } else if ("DPH".equalsIgnoreCase(position) || "SO".equalsIgnoreCase(position) || "PM".equalsIgnoreCase(position)) {
                        String query2 = "SELECT "
                                + "a.c_div_name, a.c_dept_name "
                                + "FROM app_fd_employee a WITH(NOLOCK) "
                                + "WHERE a.c_username=? "
                                + "and (a.c_position_abbr = 'DPH' or a.c_position_abbr = 'PM' or a.c_position_abbr = 'SO')";
                        ps2 = con.prepareStatement(query2);
                        ps2.setString(1, username);
                        rs2 = ps2.executeQuery();
                        
                        String div, dept;
                        
                        while (rs2.next()) {
                            c_ca_status = "Delegate to SH";
                            div = rs.getString("c_div_name");
                            dept = rs.getString("c_dept_name");
                            String query3 = "UPDATE app_fd_employee "
                                    + "SET c_ca_status=?, dateModified=GETDATE() "
                                    + "WHERE c_div_name=? "
                                    + "and c_status!='Expatriate' "
                                    + "and c_position_abbr != 'ICT' "
                                    + "and c_position_abbr != 'DPH' and c_position_abbr != 'SO' and c_position_abbr != 'PM' "
                                    + "and c_position_abbr != 'DH' and c_position_abbr != 'DDH-P' and c_position_abbr != 'DDH' and c_position_abbr != 'DCHIEF' "
                                    + "and c_ca_status = 'Delegate to DPH' "
                                    + "and c_position_abbr != 'SH' "
                                    + "and c_dept_name =?";
                                    
                            StringBuilder query4 = new StringBuilder();
                            // Cari Rangkap Jabatan DPH dan SH
                            query4
                                    .append("SELECT ")
                                    .append("a.c_div_name, a.c_position_abbr, a.c_dept_name, a.c_section_name ")
                                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                                    .append("WHERE a.c_username=? and a.c_position_abbr='SH' ");
                            ps4 = con.prepareStatement(query4.toString());
                            ps4.setString(1, username);
                            rs4 = ps4.executeQuery();
                            
                            int cnt = countDouble("SH", username);
                            // Jika Tidak Rangkap Jabatan, resultSectionName diset kosong
                            // Jika Double Jabatan, resultSectionName diset c_section_name. Lalu ditambahkahkan filter c_section_name di query2
                            if (cnt > 0) {
                                if (rs4.next()) {
                                    resultSectionName = rs4.getString("c_section_name");
                                }
                                query3 += " and c_section_name !=?";
                            } else {
                                resultSectionName = "";
                            }
//                            if(!rs4.isBeforeFirst() && rs4.getRow() == 0){
//                                resultSectionName = "";
//                            } else{
//                                if (rs4.next()) {
//                                    resultSectionName=rs4.getString("c_section_name");
//                                }
//                                query3 += " and c_section_name !=?";
//                            }
                            LogUtil.info(this.getClass().getName(), "section_name= " + resultSectionName);

                            ps3 = con.prepareStatement(query3);
                            ps3.setString(1, c_ca_status);
                            ps3.setString(2, div);
                            ps3.setString(3, dept);
                            
                            // Jika resultSectionName tidak sama dengan null, resultSectionName diset sebagai parameter ke-4 di query2
                            if(!resultSectionName.equals("")){
                                ps3.setString(4, resultSectionName);
                            }
                            ps3.executeUpdate();
                            sendDelegateEmail(div, dept, position);
                        }
                    } else if ("SH".equalsIgnoreCase(position)) {
                        String query2 = "SELECT "
                                + "a.c_div_name, a.c_dept_name "
                                + "FROM app_fd_employee a WITH(NOLOCK) "
                                + "WHERE a.c_username=? "
                                + "and (a.c_position_abbr = 'SH')";
                        ps2 = con.prepareStatement(query2);
                        ps2.setString(1, username);
                        rs2 = ps2.executeQuery();
                        
                        String div, dept;
                        
                        while (rs2.next()) {
                            c_ca_status = "Delegate to GL";
                            div = rs.getString("c_div_name");
                            dept = rs.getString("c_dept_name");
                            String query3 = "UPDATE app_fd_employee "
                                    + "SET c_ca_status=?, dateModified=GETDATE() "
                                    + "WHERE c_div_name=? "
                                    + "and c_status!='Expatriate' "
                                    + "and c_position_abbr != 'ICT' "
                                    + "and c_position_abbr != 'DPH' and c_position_abbr != 'SO' and c_position_abbr != 'PM' "
                                    + "and c_position_abbr != 'DH' and c_position_abbr != 'DDH-P' and c_position_abbr != 'DDH' and c_position_abbr != 'DCHIEF' "
                                    + "and c_ca_status = 'Delegate to SH' "
                                    + "and c_position_abbr != 'SH' "
                                    + "and c_dept_name =?";
                                    
                            StringBuilder query4 = new StringBuilder();
                            // Cari Rangkap Jabatan DPH dan SH
                            query4
                                    .append("SELECT ")
                                    .append("a.c_div_name, a.c_position_abbr, a.c_dept_name, a.c_section_name ")
                                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                                    .append("WHERE a.c_username=? and a.c_position_abbr='GL' ");
                            ps4 = con.prepareStatement(query4.toString());
                            LogUtil.info(this.getClass().getName(), "USERNYA: " + username);
                            ps4.setString(1, username);
                            rs4 = ps4.executeQuery();
                            
                            int cnt = countDouble("GL", username);
                            // Jika Tidak Rangkap Jabatan, resultSectionName diset kosong
                            // Jika Double Jabatan, resultSectionName diset c_section_name. Lalu ditambahkahkan filter c_section_name di query2
                            if (cnt > 0) {
                                if (rs4.next()) {
                                    resultSectionName = rs4.getString("c_section_name");
                                }
                                query3 += " and c_section_name !=?";
                            } else {
                                resultSectionName = "";
                            }
//                            if(!rs4.isBeforeFirst() && rs4.getRow() == 0){
//                                resultSectionName = "";
//                            } else{
//                                if (rs4.next()) {
//                                    resultSectionName=rs4.getString("c_section_name");
//                                    LogUtil.info(this.getClass().getName(), "SECTIONNYA: " + resultSectionName);
//                                }
//                                query3 += " and c_section_name !=?";
//                            }
                            LogUtil.info(this.getClass().getName(), "section_name= " + resultSectionName);

                            ps3 = con.prepareStatement(query3);
                            ps3.setString(1, c_ca_status);
                            ps3.setString(2, div);
                            ps3.setString(3, dept);
                            
                            // Jika resultSectionName tidak sama dengan null, resultSectionName diset sebagai parameter ke-4 di query2
                            if(!resultSectionName.equals("")){
                                ps3.setString(4, resultSectionName);
                            }
                            ps3.executeUpdate();
                            sendDelegateEmail(div, dept, position);
                        }
                    }else{
                        LogUtil.info(this.getClass().getName(), "Position tidak diterima");
                    }   
                } else {
                    LogUtil.info(this.getClass().getName(), "Error : div name not found");
                }

            } catch (SQLException e) {
                LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (rs3 != null) {
                    try {
                        rs3.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (rs4 != null) {
                    try {
                        rs4.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if (ps2 != null) {
                    try {
                        ps2.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (ps4 != null) {
                    try {
                        ps4.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if (ps3 != null) {
                    try {
                        ps3.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DelegateAllJobs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

    @Override
    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext()
                .getBean("workflowManager");
        WorkflowUserManager wum = (WorkflowUserManager) AppUtil.getApplicationContext()
                .getBean("workflowUserManager");
        AppService appService = (AppService) AppUtil.getApplicationContext().getBean("appService");
        String username = wum.getCurrentUsername();
        AppDefinition appDef = AppUtil.getCurrentAppDefinition();
        //WorkflowAssignment workflowAssignment = workflowManager.getAssignment(pluginName)
        //WorkflowProcess process = appService.getWorkflowProcessForApp(appDef.getAppId(), appDef.getVersion().toString(), "appraisalRequest");
        //WorkflowProcessResult wpr = workflowManager.processStart("appraisalManagement#latest#appraisalRequest");
        JSONObject mainObj = new JSONObject();
        JSONObject return_val = new JSONObject();
        try {
            //mainObj.put("id", wpr.getProcess().getId());
            //mainObj.put("instance_id", wpr.getProcess().getInstanceId());

            workflowManager.setWorkflowUserManager(wum);

            bulkDelegateJobs(workflowManager, username, mainObj);

            return_val.put("jobstart", "ok");
            return_val.write(response.getWriter());
            //mainObj.write(response.getWriter());
            //response.getWriter().write(wpr.getProcess().getId()); 
        } catch (JSONException ex) {
            LogUtil.error(getClass().getName(), ex, "error");
        }

    }

}
