/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.itasoft.tmmin.eform.am.plugin.model;

/**
 *
 * @author Tarkiman
 */
public class SummaryEnrollmentModel {

    private String divId;
    private String divName;
    private int contract1;
    private int contract2;
    private int total;
    private int totalContact1Terminate;
    private int totalContact2Terminate;
    private int totalPermanent;
    private int terminate;
    private int continueToContract2;
    private int permanent;
    private int quota;
    private int quotaRemaining;
    private int quotaUsed;

    public String getDivId() {
        return divId;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public int getContract1() {
        return contract1;
    }

    public void setContract1(int contract1) {
        this.contract1 = contract1;
    }

    public int getContract2() {
        return contract2;
    }

    public void setContract2(int contract2) {
        this.contract2 = contract2;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalContact1Terminate() {
        return totalContact1Terminate;
    }

    public void setTotalContact1Terminate(int totalContact1Terminate) {
        this.totalContact1Terminate = totalContact1Terminate;
    }

    public int getTotalContact2Terminate() {
        return totalContact2Terminate;
    }

    public void setTotalContact2Terminate(int totalContact2Terminate) {
        this.totalContact2Terminate = totalContact2Terminate;
    }

    public int getTotalPermanent() {
        return totalPermanent;
    }

    public void setTotalPermanent(int totalPermanent) {
        this.totalPermanent = totalPermanent;
    }

    public int getTerminate() {
        return terminate;
    }

    public void setTerminate(int terminate) {
        this.terminate = terminate;
    }

    public int getContinueToContract2() {
        return continueToContract2;
    }

    public void setContinueToContract2(int continueToContract2) {
        this.continueToContract2 = continueToContract2;
    }

    public int getPermanent() {
        return permanent;
    }

    public void setPermanent(int permanent) {
        this.permanent = permanent;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getQuotaRemaining() {
        return quotaRemaining;
    }

    public void setQuotaRemaining(int quotaRemaining) {
        this.quotaRemaining = quotaRemaining;
    }

    public int getQuotaUsed() {
        return quotaUsed;
    }

    public void setQuotaUsed(int quotaUsed) {
        this.quotaUsed = quotaUsed;
    }

}
