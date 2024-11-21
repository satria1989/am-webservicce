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
public class KickStartProcessCARequest extends Element implements PluginWebSupport {

    public static String pluginName = "TMMIN - AM - Bulk Start Process ";

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
        }
        //modify 20221021 : cari approval untuk DH 
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

    public void bulkStartProcess(WorkflowManager workflowManager, String username, JSONObject mainObj) {

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");

        StartProcessCAJobs ex = new StartProcessCAJobs(this,workflowManager, username, ds);
        Thread t1 = new Thread(ex);
        t1.start();
        LogUtil.info(getClass().getName(), "################################");
    }

    public class StartProcessCAJobs implements Runnable {

        WorkflowManager workflowManager;
        String username;
        DataSource ds;
        KickStartProcessCARequest parentThread;

        public StartProcessCAJobs(KickStartProcessCARequest parentThread,WorkflowManager workflowManager, String username, DataSource ds) {
            this.workflowManager = workflowManager;
            this.username = username;
            this.ds = ds;
            this.parentThread = parentThread;
        }

        @Override
        public void run() {
            StringBuilder query = new StringBuilder();
            query
                    .append("SELECT ")
                    .append("a.c_div_name ")
                    .append("FROM app_fd_employee a WITH(NOLOCK) ")
                    .append("WHERE a.c_username=? ");

            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                Connection con = ds.getConnection();
                ps = con.prepareStatement(query.toString());
                ps.setString(1, username);
                rs = ps.executeQuery();
                boolean error = false;

                //try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    JSONArray dataToStart = new JSONArray();
                    String resultDivName = rs.getString("c_div_name");
                    LogUtil.info(this.getClass().getName(), "div name: " + resultDivName);
                    StringBuilder query2 = new StringBuilder();
                    String currentYear = "" + Year.now().getValue();
                    LogUtil.info(this.getClass().getName(), "Current Year : " + currentYear);
                    query2
                            .append("SELECT "
                                    + "distinct afe.c_noreg, "
                                    + "afe.id, "
                                    + "afe.c_name, "
                                    + "afe.c_dept_name, "
                                    + "afe.c_section_name, "
                                    + "afe.c_position_abbr, "
                                    + "afe.c_class, "
                                    + "afar.c_ca_input, "
                                    + "afar.c_ca_rev1, "
                                    + "afar.c_ca_rev2, "
                                    + "(SELECT TOP 1 afacs.c_ca_score FROM app_fd_am_ca_score afacs WHERE afacs.c_noreg=afar.c_noreg ORDER BY afacs.c_ca_year DESC) as ca_score_1, "
                                    + "(SELECT TOP 1 z.c_ca_score "
                                    + "	FROM ( "
                                    + "		SELECT TOP 2 afacs.c_ca_score, afacs.c_ca_year FROM app_fd_am_ca_score afacs WHERE afacs.c_noreg=afar.c_noreg ORDER BY afacs.c_ca_year DESC "
                                    + "	) z "
                                    + "	ORDER BY z.c_ca_year ASC "
                                    + ") as ca_score_2, "
                                    + "CASE  "
                                    + "	WHEN afar.c_ca_input>0 THEN 'sudah_dilakukan_penilaian' "
                                    + "	WHEN afe.c_noreg=afard.c_noreg THEN 'sudah_dilakukan_penilaian' "
                                    + "	ELSE 'belum_dilakukan_penilaian' "
                                    + "END AS status_penilaian "
                                    + "FROM app_fd_employee afe LEFT JOIN app_fd_am_request afar "
                                    + "ON afe.c_noreg=afar.c_noreg "
                                    + "LEFT JOIN app_fd_am_request_detail afard "
                                    + "ON afe.c_noreg=afard.c_noreg "
                                    + "WHERE afe.c_div_name=? "
                                    + "and ( "
                                    + "    afe.c_class LIKE '3%' "
                                    + "    or afe.c_class LIKE '4%' "
                                    + "    or afe.c_class LIKE '5%' "
                                    + "    or afe.c_class LIKE '6%' "
                                    + "    or afe.c_class LIKE '7%' "
                                    + "    or afe.c_class LIKE '8%' "
                                    + "    or afe.c_position_abbr='GL' "
                                    + "    or afe.c_position_abbr='TL' "
                                    + ") and afe.c_status!='Expatriate' "
                                    + "and (afe.c_ca_year is null or afe.c_ca_year!=?)");

                    PreparedStatement ps2 = con.prepareStatement(query2.toString());
                    ps2.setString(1, resultDivName);
                    ps2.setString(2, currentYear);
                    ResultSet rs2 = null;
                    try {
                        rs2 = ps2.executeQuery();
                        ResultSetMetaData rsmd = rs2.getMetaData();
                        int columnCount = rsmd.getColumnCount();

                        while (rs2.next()) {
                            if (rs2.getString("status_penilaian") != null && rs2.getString("status_penilaian").equals("belum_dilakukan_penilaian") && rs2.getString("c_noreg") != null) {
                                JSONObject objTostart = new JSONObject();
                                /*
                                for (int i = 1; i <= columnCount; i++) {

                                    //objTostart.put(rsmd.getColumnLabel(i), rs2.getString(rsmd.getColumnLabel(i)));
                                }*/
                                String uuid = UuidGenerator.getInstance().getUuid();
                                objTostart.put("uuid", uuid);

                                LogUtil.info(this.getClass().getName(), "prepare start job  : " + uuid);

                                String noreg = rs2.getString("c_noreg");

                                objTostart.put("approvermappingdata", parentThread.getJSONObjectApproverMapping(noreg, username));

                                dataToStart.put(objTostart);
                            }
                        }

                    } catch (JSONException ex) {
                        LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
                        error = true;
                    } finally {
                        if (rs2 != null) {
                            rs2.close();
                        }
                        if (ps2 != null) {
                            ps2.close();
                        }
                    }

                    //process kick start
                    if (!error) {
                        LogUtil.info(this.getClass().getName(), "Begin Kick Start Process ");
                        ArrayList<String> noregError = new ArrayList<>();
                        for (int i = 0; i < dataToStart.length(); i++) {
                            PreparedStatement ps3 = null, ps4 = null;
                            String c_noreg = "";
                            try {
                                JSONObject objTostart = dataToStart.getJSONObject(i);
                                JSONObject objApproverMapping = objTostart.getJSONObject("approvermappingdata");
                                String uuid = objTostart.getString("uuid");

                                String c_division = objApproverMapping.getJSONObject("employee").getString("div_name");
                                String c_department = objApproverMapping.getJSONObject("employee").getString("dept_name");
                                String c_name = objApproverMapping.getJSONObject("employee").getString("name");
                                c_noreg = objApproverMapping.getJSONObject("employee").getString("noreg");
                                String c_class = objApproverMapping.getJSONObject("employee").getString("class");
                                String c_age = objApproverMapping.getJSONObject("employee").getString("age");
                                String c_labor_type = objApproverMapping.getJSONObject("employee").getString("labor_type");
                                String c_section_name = objApproverMapping.getJSONObject("employee").getString("section_name");
                                String c_position = objApproverMapping.getJSONObject("employee").getString("position_abbr");
                                String c_approver_dph = "";
                                String c_approver_sh = "";
                                String c_approver_dh = "";
                                JSONArray list_approvers = objApproverMapping.getJSONArray("list_approvers");
                                for (int j = 0; j < list_approvers.length(); j++) {
                                    JSONObject objApprover = list_approvers.getJSONObject(j);
                                    Iterator keys = objApprover.keys();
                                    while (keys.hasNext()) {
                                        String key = (String) keys.next();
                                        LogUtil.info("Debug:", "key :" + key + ", value= " + objApprover.getString(key));
                                    }
                                    if (objApprover.getString("position").equals("DPH")) {
                                        if (c_approver_dph.isEmpty()) {
                                            c_approver_dph = objApprover.getString("username");
                                        } else {
                                            c_approver_dph += "," + objApprover.getString("username");
                                        }
                                    } else if (objApprover.getString("position").equals("SH")) {
                                        if (c_approver_sh.isEmpty()) {
                                            c_approver_sh = objApprover.getString("username");
                                        } else {
                                            c_approver_sh += "," + objApprover.getString("username");
                                        }
                                    } else if (objApprover.getString("position").equals("DH")) {
                                        if (c_approver_dh.isEmpty()) {
                                            c_approver_dh = objApprover.getString("username");
                                        } else {
                                            c_approver_dh += "," + objApprover.getString("username");
                                        }
                                    }
                                }
                                String c_approver_hr_dh = objApproverMapping.getJSONObject("hrd").has("approver_hr_dh") ? objApproverMapping.getJSONObject("hrd").getJSONObject("approver_hr_dh").getString("username") : "admin";
                                String c_approver_hr_dph = objApproverMapping.getJSONObject("hrd").has("approver_hr_dph")?objApproverMapping.getJSONObject("hrd").getJSONObject("approver_hr_dph").getString("username"):"admin";
                                
                                LogUtil.info(this.getClass().getName(), "c_approver_hr_dph= "+c_approver_hr_dph+", c_approver_hr_dh="+c_approver_hr_dh);
                                
                                String c_requester = username;
                                String c_status = "individu";
                                String c_last_status = "Submitted";
                                String c_latest_status = "Submitted";
                                String c_type = "delegate";
                                String c_last_activity = "Submit Request Appraisal";
                                //String c_request_number = "";
                                String name = objApproverMapping.getJSONObject("current_user").getString("name");

                                //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ZZZ").format(new java.util.Date());
                                java.sql.Time currentTime = new java.sql.Time(new java.util.Date().getTime());

                                java.util.Map<java.lang.String, java.lang.String> variables = new java.util.HashMap<java.lang.String, java.lang.String>();

                                variables.put("type", "delegate");
                                variables.put("approver_dh", c_approver_dh);
                                variables.put("approver_dph", c_approver_dph);
                                variables.put("approver_sh", c_approver_sh);
                                variables.put("approver_hr_dh", c_approver_hr_dh);
                                variables.put("approver_hr_dph", c_approver_hr_dph);
                                variables.put("requester", c_requester);

                                WorkflowProcessResult wpr = workflowManager.processStart("appraisalManagement#latest#appraisalRequest", variables, username);

                                String recordId = wpr.getProcess().getInstanceId();

                                //workflowManager.processVariable(recordId, "type", "delegate");
                                StringBuilder query3 = new StringBuilder();
                                query3
                                        .append("INSERT INTO app_fd_am_request "
                                                + "(id,dateCreated,dateModified,createdBy,createdByName,modifiedBy,modifiedByName,"
                                                + "c_division,c_department,c_name,c_noreg,c_class,c_age,c_labor_type,c_section_name,c_position,"
                                                + "c_approver_dph,c_approver_sh,c_approver_dh,c_approver_hr_dh,c_requester,c_approver_hr_dph,"
                                                + "c_status, c_last_status, c_latest_status, c_type, c_last_activity) "
                                                + "VALUES (?, GETDATE(), GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                                ps3 = con.prepareStatement(query3.toString());
                                ps3.setString(1, recordId);
                                //ps3.setTime(2, currentTime);
                                //ps3.setTime(3, currentTime);
                                ps3.setString(2, username);
                                ps3.setString(3, name);
                                ps3.setString(4, username);
                                ps3.setString(5, name);
                                ps3.setString(6, c_division);
                                ps3.setString(7, c_department);
                                ps3.setString(8, c_name);
                                ps3.setString(9, c_noreg);
                                ps3.setString(10, c_class);
                                ps3.setString(11, c_age);
                                ps3.setString(12, c_labor_type);
                                ps3.setString(13, c_section_name);
                                ps3.setString(14, c_position);
                                ps3.setString(15, c_approver_dph);
                                ps3.setString(16, c_approver_sh);
                                ps3.setString(17, c_approver_dh);
                                ps3.setString(18, c_approver_hr_dh);
                                ps3.setString(19, c_requester);
                                ps3.setString(20, c_approver_hr_dph);

                                ps3.setString(21, c_status);
                                ps3.setString(22, c_last_status);
                                ps3.setString(23, c_latest_status);
                                ps3.setString(24, c_type);
                                ps3.setString(25, c_last_activity);

                                ps3.executeUpdate();
                                // save audit trail 
                                StringBuilder query4 = new StringBuilder();
                                query4
                                        .append("INSERT INTO app_fd_am_audit_trail "
                                                + "(id,dateCreated,dateModified,createdBy,createdByName,modifiedBy,modifiedByName,"
                                                + "c_activity_name,c_parent_id,c_name,c_remark,c_approved_date,c_username,c_status) "
                                                + "VALUES (?, GETDATE(), GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                                ps4 = con.prepareStatement(query4.toString());

                                //LogUtil.info("Debug:", "key :" + key + ", value= " + objApprover.getString(key));
                                ps4.setString(1, uuid);
                                ps4.setString(2, username);
                                ps4.setString(3, name);
                                ps4.setString(4, username);
                                ps4.setString(5, name);
                                ps4.setString(6, "Submit");
                                ps4.setString(7, recordId);
                                ps4.setString(8, name);
                                ps4.setString(9, "");
                                ps4.setString(10, "");
                                ps4.setString(11, username);
                                ps4.setString(12, c_last_status);

                                ps4.executeUpdate();

                            } catch (Exception ex) {
                                LogUtil.error(this.getClass().getName(), ex, "Error (noreg "+c_noreg+"): " + ex.getMessage());
                                noregError.add(c_noreg);
                            } finally {
                                if (ps3 != null) {
                                    ps3.close();
                                }
                                if (ps4 != null) {
                                    ps4.close();
                                }
                            }

                        }

                        PreparedStatement ps5 = null;
                        try {
                            LogUtil.info(this.getClass().getName(), "Updating CA flag... ");
                            
                            String query5 = "UPDATE app_fd_employee "
                                    + "SET c_ca_year=?, dateModified=GETDATE() "
                                    + "WHERE c_div_name=? "
                                    + "and ( "
                                    + "    c_class LIKE '3%' "
                                    + "    or c_class LIKE '4%' "
                                    + "    or c_class LIKE '5%' "
                                    + "    or c_class LIKE '6%' "
                                    + "    or c_class LIKE '7%' "
                                    + "    or c_class LIKE '8%' "
                                    + "    or c_position_abbr='GL' "
                                    + "    or c_position_abbr='TL' "
                                    + ") and c_status!='Expatriate' ";
                            
                            String noregErrorCriteria = "";
                            if(noregError.size()>0){
                                for(String noreg :noregError){
                                    if(noregErrorCriteria.isEmpty()){
                                        noregErrorCriteria = "and c_noreg not in ('"+noreg+"'";
                                    }else{
                                        noregErrorCriteria += ",'"+noreg+"'";
                                    }
                                }
                                
                                noregErrorCriteria +=")";
                                query5 += noregErrorCriteria;
                            }

                            ps5 = con.prepareStatement(query5);

                            ps5.setString(1, currentYear);
                            ps5.setString(2, resultDivName);

                            ps5.executeUpdate();
                        } catch (SQLException ex) {
                            LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
                        } finally {
                            if (ps5 != null) {
                                ps5.close();
                            }
                        }
                    }

                    LogUtil.info(this.getClass().getName(), "Done Kick Start Process ");

                } else {
                    LogUtil.info(this.getClass().getName(), "Error : div name not found");
                }

                //} catch (SQLException e) {
                //    LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
                //}
            } catch (SQLException e) {
                LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(KickStartProcessCARequest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(KickStartProcessCARequest.class.getName()).log(Level.SEVERE, null, ex);
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

            bulkStartProcess(workflowManager, username, mainObj);

            return_val.put("jobstart", "ok");
            return_val.write(response.getWriter());
            //mainObj.write(response.getWriter());
            //response.getWriter().write(wpr.getProcess().getId()); 
        } catch (JSONException ex) {
            LogUtil.error(getClass().getName(), ex, "error");
        }

    }

}
