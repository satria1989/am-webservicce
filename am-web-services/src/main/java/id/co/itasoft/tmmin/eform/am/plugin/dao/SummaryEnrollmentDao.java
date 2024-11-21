/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.itasoft.tmmin.eform.am.plugin.dao;

import id.co.itasoft.tmmin.eform.am.plugin.model.ParameterModel;
import id.co.itasoft.tmmin.eform.am.plugin.model.SummaryEnrollmentModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

/**
 *
 * @author Tarkiman
 */
public class SummaryEnrollmentDao {

    public List<SummaryEnrollmentModel> getSummaryEnrollmentAllDivision(ParameterModel param) {

        /**
         * QUERY FOR TESTING
         */

        /*        
SELECT 
a.c_directorate_id AS directorate_id,
a.c_div_id AS div_id, 
a.c_division AS div_name, 
(
	SELECT SUM(CONVERT(INT,q.c_quota))
	FROM app_fd_er_master_quota q WITH(NOLOCK)
	WHERE CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,q.c_valid_from,103) AND  CONVERT(date,q.c_valid_to,103)
	AND q.c_division=a.c_div_id
) AS quota,
( 
	SELECT COUNT(b.c_present_status) 
	FROM app_fd_er_request b WITH (NOLOCK) 
	JOIN app_fd_er_master_quota x WITH (NOLOCK) ON x.c_division=b.c_div_id
	WHERE b.c_div_id=a.c_div_id 
	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,x.c_valid_from,103) AND  CONVERT(date,x.c_valid_to,103))
	AND b.c_present_status='Kontrak 1.2' 
	AND b.c_no_surat IS NOT NULL 
	AND b.c_no_surat != '' 
	AND b.c_div_id='#divisi#' 
	AND b.c_dept_id='#department#' 
	AND b.c_directorate_id='#directorateId#'
	AND b.c_noreg='#noreg#' 
	AND b.c_present_status='#statusPKWT#' 
	AND b.c_process_enrollment='#processEnrollment#' 
	AND (CONVERT(DATE,b.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND (CONVERT(DATE,'#tanggalPengajuanTo#',103))) 
) AS contract_1, 
( 
	SELECT COUNT(c.c_present_status) 
	FROM app_fd_er_request c WITH (NOLOCK) 
	JOIN app_fd_er_master_quota y WITH (NOLOCK) ON y.c_division=c.c_div_id
	WHERE c.c_div_id=a.c_div_id 
	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,y.c_valid_from,103) AND  CONVERT(date,y.c_valid_to,103))
	AND c.c_present_status='Kontrak 2' 
	AND c.c_no_surat IS NOT NULL 
	AND c.c_no_surat != '' 
	AND c.c_div_id='#divisi#' 
	AND c.c_dept_id='#department#' 
	AND c.c_directorate_id='#directorateId#'
	AND c.c_noreg='#noreg#' 
	AND c.c_present_status='#statusPKWT#' 
	AND c.c_process_enrollment='#processEnrollment#' 
	AND (CONVERT(DATE,c.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND (CONVERT(DATE,'#tanggalPengajuanTo#',103))) 
) AS contract_2 
FROM app_fd_er_request a WITH (NOLOCK) 
JOIN app_fd_er_master_quota z WITH (NOLOCK) ON z.c_division=a.c_div_id
WHERE a.c_no_surat IS NOT NULL AND a.c_no_surat != '' 
AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,z.c_valid_from,103) AND  CONVERT(date,z.c_valid_to,103))
AND a.c_div_id='#divisi#' 
AND a.c_dept_id='#department#' 
AND a.c_directorate_id='#directorateId#'
AND a.c_noreg='#noreg#' 
AND a.c_present_status='#statusPKWT#' 
AND a.c_process_enrollment='#processEnrollment#' 
AND (CONVERT(DATE,a.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND (CONVERT(DATE,'#tanggalPengajuanTo#',103))) 
GROUP BY a.c_div_id,a.c_division,a.c_directorate_id 
ORDER BY a.c_division ASC
         */
        List<SummaryEnrollmentModel> list = new ArrayList<SummaryEnrollmentModel>();

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query.append(" SELECT ");
        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" a.c_directorate_id AS directorate_id, ");
        }
        query.append(" a.c_div_id AS div_id, ");
        query.append(" a.c_division AS div_name, ");
        query.append(" ( ");
        query.append(" SELECT SUM(CONVERT(INT,q.c_quota)) ");
        query.append(" FROM app_fd_er_master_quota q WITH(NOLOCK) ");
        query.append(" WHERE CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,q.c_valid_from,103) AND  CONVERT(date,q.c_valid_to,103) ");
        query.append(" AND q.c_division=a.c_div_id ");
        query.append(" ) AS quota, ");
        query.append(" ( ");
        query.append(" 	SELECT COUNT(r.c_present_status) ");
        query.append(" 	FROM app_fd_er_request r WITH (NOLOCK) ");
        query.append(" 	JOIN app_fd_er_master_quota i WITH (NOLOCK) ON i.c_section=r.c_section  ");
        query.append(" 	WHERE r.c_div_id=a.c_div_id ");
        query.append(" 	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,i.c_valid_from,103) AND  CONVERT(date,i.c_valid_to,103)) ");
//query.append(" 	AND r.c_present_status='Kontrak 1.2' "); 
        query.append(" 	AND r.c_no_surat IS NOT NULL ");
        query.append(" 	AND r.c_no_surat != '' ");
        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND r.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND r.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND r.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND r.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND r.c_present_status='" + param.getStatusPkwt() + "' ");
        }
//        query.append(" 	AND r.c_div_id='#divisi#' ");        
//        query.append(" 	AND r.c_dept_id='#department#' ");        
//        query.append(" 	AND r.c_directorate_id='#directorateId#' ");
//        query.append(" 	AND r.c_noreg='#noreg#' ");        
//        query.append(" 	AND r.c_present_status='#statusPKWT#' ");        
        query.append(" 	AND r.c_process_enrollment='Permanent' ");
        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,r.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }
        //query.append(" 	AND (CONVERT(DATE,r.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND (CONVERT(DATE,'#tanggalPengajuanTo#',103))) ");
        query.append(" ) AS quota_used, ");
        query.append(" ( ");
        query.append(" 	SELECT COUNT(b.c_present_status) ");
        query.append(" 	FROM app_fd_er_request b WITH (NOLOCK) ");
        query.append(" 	JOIN app_fd_er_master_quota x WITH (NOLOCK) ON x.c_section=b.c_section ");
        query.append(" 	WHERE b.c_div_id=a.c_div_id ");
        query.append(" 	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,x.c_valid_from,103) AND  CONVERT(date,x.c_valid_to,103)) ");
        query.append(" 	AND b.c_present_status='Kontrak 1.2' ");
        query.append(" 	AND b.c_no_surat IS NOT NULL ");
        query.append(" 	AND b.c_no_surat != '' ");
        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND b.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND b.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND b.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND b.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND b.c_present_status='" + param.getStatusPkwt() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getProcessEnrollment()) && param.getProcessEnrollment() != null) {
            query.append(" AND b.c_process_enrollment='" + param.getProcessEnrollment() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,b.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }
        query.append(" ) AS contract_1, ");
        query.append(" ( ");
        query.append(" 	SELECT COUNT(c.c_present_status) ");
        query.append(" 	FROM app_fd_er_request c WITH (NOLOCK) ");
        query.append(" 	JOIN app_fd_er_master_quota y WITH (NOLOCK) ON y.c_section=c.c_section ");
        query.append(" 	WHERE c.c_div_id=a.c_div_id ");
        query.append(" 	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,y.c_valid_from,103) AND  CONVERT(date,y.c_valid_to,103)) ");
        query.append(" 	AND c.c_present_status='Kontrak 2' ");
        query.append(" 	AND c.c_no_surat IS NOT NULL ");
        query.append(" 	AND c.c_no_surat != '' ");
        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND c.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND c.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND c.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND c.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND c.c_present_status='" + param.getStatusPkwt() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getProcessEnrollment()) && param.getProcessEnrollment() != null) {
            query.append(" AND c.c_process_enrollment='" + param.getProcessEnrollment() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,c.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }
        query.append(" ) AS contract_2 ");
        query.append(" FROM app_fd_er_request a WITH (NOLOCK) ");
        query.append(" JOIN app_fd_er_master_quota z WITH (NOLOCK) ON z.c_section=a.c_section ");
        query.append(" WHERE a.c_no_surat IS NOT NULL AND a.c_no_surat != '' ");
        query.append(" AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,z.c_valid_from,103) AND  CONVERT(date,z.c_valid_to,103)) ");

        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND a.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND a.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND a.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND a.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND a.c_present_status='" + param.getStatusPkwt() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getProcessEnrollment()) && param.getProcessEnrollment() != null) {
            query.append(" AND a.c_process_enrollment='" + param.getProcessEnrollment() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,a.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }

        query.append(" GROUP BY a.c_div_id,a.c_division ");
        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" ,a.c_directorate_id ");
        }
        query.append(" ORDER BY a.c_division ASC");

        //LogUtil.info("QUERY 1 : ", query.toString());
        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {

            try (ResultSet rs = ps.executeQuery()) {

                SummaryEnrollmentModel s;

                while (rs.next()) {
                    s = new SummaryEnrollmentModel();
                    s.setDivId(rs.getString("div_id"));
                    s.setDivName(rs.getString("div_name"));
                    s.setContract1(rs.getInt("contract_1"));
                    s.setContract2(rs.getInt("contract_2"));
                    s.setTotal(s.getContract1() + s.getContract2());
                    s.setQuota(rs.getInt("quota"));
                    s.setQuotaUsed(rs.getInt("quota_used"));
                    list.add(s);
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return list;
    }

    /**
     * QUERY FOR TESTINING METHOD getSummaryEnrollment
     *
     * @param param
     * @return
     *
     *
     * SELECT ( SELECT COUNT(a.c_process_enrollment)
     * total_continue_to_contract_2 FROM app_fd_er_request a WITH (NOLOCK) JOIN
     * app_fd_er_master_quota w WITH (NOLOCK) ON w.c_division=a.c_div_id WHERE
     * a.c_present_status='Kontrak 1.2' AND a.c_process_enrollment='Contract_2'
     * AND a.c_no_surat IS NOT NULL AND a.c_no_surat != '' AND
     * (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,w.c_valid_from,103) AND
     * CONVERT(date,w.c_valid_to,103)) AND a.c_div_id='#divisi#' AND
     * a.c_dept_id='#department#' AND a.c_directorate_id='#directorateId#' AND
     * a.c_noreg='#noreg#' AND a.c_present_status='#statusPKWT#' AND
     * a.c_process_enrollment='#processEnrollment#' AND
     * (CONVERT(DATE,a.c_tgl_pengajuan,103) BETWEEN
     * (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND
     * (CONVERT(DATE,'#tanggalPengajuanTo#',103))) ) AS
     * total_continue_to_contract_2, ( SELECT COUNT(b.c_process_enrollment)
     * total_kontrak_1_terminate FROM app_fd_er_request b WITH (NOLOCK) JOIN
     * app_fd_er_master_quota x WITH (NOLOCK) ON x.c_division=b.c_div_id WHERE
     * b.c_present_status='Kontrak 1.2' AND b.c_process_enrollment='Ended' AND
     * b.c_no_surat IS NOT NULL AND b.c_no_surat != '' AND
     * (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,x.c_valid_from,103) AND
     * CONVERT(date,x.c_valid_to,103)) AND b.c_div_id='#divisi#' AND
     * b.c_dept_id='#department#' AND b.c_directorate_id='#directorateId#' AND
     * b.c_noreg='#noreg#' AND b.c_present_status='#statusPKWT#' AND
     * b.c_process_enrollment='#processEnrollment#' AND
     * (CONVERT(DATE,b.c_tgl_pengajuan,103) BETWEEN
     * (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND
     * (CONVERT(DATE,'#tanggalPengajuanTo#',103))) ) AS
     * total_kontrak_1_terminate, ( SELECT COUNT(c.c_process_enrollment)
     * total_permanent FROM app_fd_er_request c WITH (NOLOCK) JOIN
     * app_fd_er_master_quota y WITH (NOLOCK) ON y.c_division=c.c_div_id WHERE
     * c.c_present_status='Kontrak 2' AND c.c_process_enrollment='Permanent' AND
     * c.c_no_surat IS NOT NULL AND c.c_no_surat != '' AND
     * (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,y.c_valid_from,103) AND
     * CONVERT(date,y.c_valid_to,103)) AND c.c_div_id='#divisi#' AND
     * c.c_dept_id='#department#' AND c.c_directorate_id='#directorateId#' AND
     * c.c_noreg='#noreg#' AND c.c_present_status='#statusPKWT#' AND
     * c.c_process_enrollment='#processEnrollment#' AND
     * (CONVERT(DATE,c.c_tgl_pengajuan,103) BETWEEN
     * (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND
     * (CONVERT(DATE,'#tanggalPengajuanTo#',103))) ) AS total_permanent, (
     * SELECT COUNT(d.c_process_enrollment) total_kontrak_2_terminate FROM
     * app_fd_er_request d WITH (NOLOCK) JOIN app_fd_er_master_quota z WITH
     * (NOLOCK) ON z.c_division=d.c_div_id WHERE d.c_present_status='Kontrak 2'
     * AND d.c_process_enrollment='Ended' AND d.c_no_surat IS NOT NULL AND
     * d.c_no_surat != '' AND (CONVERT(date,GETDATE(),103) BETWEEN
     * CONVERT(date,z.c_valid_from,103) AND CONVERT(date,z.c_valid_to,103)) AND
     * d.c_div_id='#divisi#' AND d.c_dept_id='#department#' AND
     * d.c_directorate_id='#directorateId#' AND d.c_noreg='#noreg#' AND
     * d.c_present_status='#statusPKWT#' AND
     * d.c_process_enrollment='#processEnrollment#' AND
     * (CONVERT(DATE,d.c_tgl_pengajuan,103) BETWEEN
     * (CONVERT(DATE,'#tanggalPengajuanFrom#',103)) AND
     * (CONVERT(DATE,'#tanggalPengajuanTo#',103))) ) AS
     * total_kontrak_2_terminate
     */
    public SummaryEnrollmentModel getSummaryEnrollment(ParameterModel param) {

        SummaryEnrollmentModel s = new SummaryEnrollmentModel();

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query
                .append(" SELECT ")
                .append(" ( ")
                .append(" 	SELECT ")
                .append(" 	COUNT(a.c_process_enrollment) total_continue_to_contract_2 ")
                .append(" 	FROM app_fd_er_request a  WITH (NOLOCK) ")
                .append(" 	JOIN app_fd_er_master_quota w WITH (NOLOCK) ON w.c_section=a.c_section ")
                .append(" 	WHERE a.c_present_status='Kontrak 1.2' AND a.c_process_enrollment='Contract_2' AND a.c_no_surat IS NOT NULL AND a.c_no_surat != '' ")
                .append(" 	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,w.c_valid_from,103) AND  CONVERT(date,w.c_valid_to,103)) ");
        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND a.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND a.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND a.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND a.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND a.c_present_status='" + param.getStatusPkwt() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getProcessEnrollment()) && param.getProcessEnrollment() != null) {
            query.append(" AND a.c_process_enrollment='" + param.getProcessEnrollment() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,a.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }

        query.append(" ) AS total_continue_to_contract_2, ")
                .append(" ( ")
                .append(" 	SELECT ")
                .append(" 	COUNT(b.c_process_enrollment) total_kontrak_1_terminate ")
                .append(" 	FROM app_fd_er_request b  WITH (NOLOCK) ")
                .append(" 	JOIN app_fd_er_master_quota x WITH (NOLOCK) ON x.c_section=b.c_section ")
                .append(" 	WHERE b.c_present_status='Kontrak 1.2' AND b.c_process_enrollment='Ended' AND b.c_no_surat IS NOT NULL AND b.c_no_surat != '' ")
                .append(" 	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,x.c_valid_from,103) AND  CONVERT(date,x.c_valid_to,103)) ");
        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND b.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND b.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND b.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND b.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND b.c_present_status='" + param.getStatusPkwt() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getProcessEnrollment()) && param.getProcessEnrollment() != null) {
            query.append(" AND b.c_process_enrollment='" + param.getProcessEnrollment() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,b.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }

        query.append(" ) AS total_kontrak_1_terminate, ")
                .append(" ( ")
                .append(" 	SELECT ")
                .append(" 	COUNT(c.c_process_enrollment) total_permanent ")
                .append(" 	FROM app_fd_er_request c  WITH (NOLOCK) ")
                .append(" 	JOIN app_fd_er_master_quota y WITH (NOLOCK) ON y.c_section=c.c_section ")
                .append(" 	WHERE c.c_present_status='Kontrak 2' AND c.c_process_enrollment='Permanent' AND c.c_no_surat IS NOT NULL AND c.c_no_surat != '' ")
                .append(" 	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,y.c_valid_from,103) AND  CONVERT(date,y.c_valid_to,103)) ");
        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND c.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND c.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND c.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND c.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND c.c_present_status='" + param.getStatusPkwt() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getProcessEnrollment()) && param.getProcessEnrollment() != null) {
            query.append(" AND c.c_process_enrollment='" + param.getProcessEnrollment() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,c.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }

        query.append(" ) AS total_permanent, ")
                .append(" ( ")
                .append(" 	SELECT ")
                .append(" 	COUNT(d.c_process_enrollment) total_kontrak_2_terminate ")
                .append(" 	FROM app_fd_er_request d WITH (NOLOCK) ")
                .append(" 	JOIN app_fd_er_master_quota z WITH (NOLOCK) ON z.c_section=d.c_section ")
                .append(" 	WHERE d.c_present_status='Kontrak 2' AND d.c_process_enrollment='Ended' AND d.c_no_surat IS NOT NULL AND d.c_no_surat != '' ")
                .append(" 	AND (CONVERT(date,GETDATE(),103) BETWEEN CONVERT(date,z.c_valid_from,103) AND  CONVERT(date,z.c_valid_to,103)) ");
        if (!"".equalsIgnoreCase(param.getDivId()) && param.getDivId() != null) {
            query.append(" AND d.c_div_id='" + param.getDivId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDeptId()) && param.getDeptId() != null) {
            query.append(" AND d.c_dept_id='" + param.getDeptId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getDirectorateId()) && param.getDirectorateId() != null) {
            query.append(" AND d.c_directorate_id='" + param.getDirectorateId() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getNoreg()) && param.getNoreg() != null) {
            query.append(" AND d.c_noreg='" + param.getNoreg() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getStatusPkwt()) && param.getStatusPkwt() != null) {
            query.append(" AND d.c_present_status='" + param.getStatusPkwt() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getProcessEnrollment()) && param.getProcessEnrollment() != null) {
            query.append(" AND d.c_process_enrollment='" + param.getProcessEnrollment() + "' ");
        }

        if (!"".equalsIgnoreCase(param.getTanggalPengajuanFrom()) && param.getTanggalPengajuanFrom() != null && !"".equalsIgnoreCase(param.getTanggalPengajuanTo()) && param.getTanggalPengajuanTo() != null) {
            query.append(" AND (CONVERT(DATE,d.c_tgl_pengajuan,103) BETWEEN (CONVERT(DATE,'" + param.getTanggalPengajuanFrom() + "',103)) AND (CONVERT(DATE,'" + param.getTanggalPengajuanTo() + "',103))) ");
        }
        query.append(" ) AS total_kontrak_2_terminate ");

        //LogUtil.info("QUERY 2 : ", query.toString());
        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    s = new SummaryEnrollmentModel();
                    s.setContinueToContract2(rs.getInt("total_continue_to_contract_2"));
                    s.setTotalContact1Terminate(rs.getInt("total_kontrak_1_terminate"));
                    s.setTotalPermanent(rs.getInt("total_permanent"));
                    s.setTotalContact2Terminate(rs.getInt("total_kontrak_2_terminate"));

                    s.setTotal(s.getContract1() + s.getContract2());
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return s;
    }

}
