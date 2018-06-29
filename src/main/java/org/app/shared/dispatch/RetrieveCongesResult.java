package org.app.shared.dispatch;

import java.util.List;

import org.app.shared.wrapper.CongesWrapper;

import com.gwtplatform.dispatch.rpc.shared.Result;

public class RetrieveCongesResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<CongesWrapper> congesList;
	private double cpPris;
	private double previousCpPris;
	private double cpSolde;
	private double previousCpSolde;
	private double rttPris;
	private double rttSolde;
	private double congesPris;
	private double congesSolde;
	@SuppressWarnings("unused")
	private RetrieveCongesResult() {
		// For serialization only
	}

	public RetrieveCongesResult(List<CongesWrapper> congesList, double cpPris, double previousCpPris,
			double cpSolde, double previousCpSolde, double rttPris, double rttSolde, double congesPris, double congesSolde) {
		this.congesList = congesList;
		this.cpPris = cpPris;
		this.previousCpPris = previousCpPris;
		this.cpSolde = cpSolde;
		this.previousCpSolde = previousCpSolde;
		this.rttPris = rttPris;
		this.rttSolde = rttSolde;
		this.congesPris =congesPris;
		this.congesSolde = congesSolde;
	}
	
	public double getCongesSolde() {
		return congesSolde;
	}
	public void setCongesSolde(double congesSolde) {
		this.congesSolde = congesSolde;
	}

	public List<CongesWrapper> getResult() {
		return congesList;
	}

	public double getCpPris() {
		return cpPris;
	}

	public double getPreviousCpPris() {
		return previousCpPris;
	}

	public double getCpSolde() {
		return cpSolde;
	}

	public double getPreviousCpSolde() {
		return previousCpSolde;
	}

	public double getRttPris() {
		return rttPris;
	}

	public double getRttSolde() {
		return rttSolde;
	}

	public double getCongesPris() {
		return congesPris;
	}


}
