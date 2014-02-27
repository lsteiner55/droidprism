package com.droidprism;


import java.io.Serializable;
import java.util.ArrayList;

class MccMnc implements Serializable {
	public static final long serialVersionUID = 1L;
	int mcc;
	int mnc;
	String iso;
	ArrayList<String> operator = new ArrayList<String>();
	ArrayList<APN> apn = new ArrayList<APN>();
	ArrayList<String> smsemail = new ArrayList<String>();
	ArrayList<String> mmsemail = new ArrayList<String>();

	public MccMnc(int mcc, int mnc, String iso, String operator) {
		this.mcc = mcc;
		this.mnc = mnc;
		this.iso = iso;
		if (operator.length() > 0) {
			this.operator.add(operator);
		}

	}

	public MccMnc(int mcc, int mnc, String iso, ArrayList<String> operator) {
		this(mcc, mnc, iso, "");
		for (String s : operator) {
			this.addoperator(s);
		}

	}

	public void addoperator(String operator) {
		if (!this.operator.contains(operator) && operator.length() > 0) {
			this.operator.add(operator);
		}
	}

	public void addapn(APN a) {
		if (!this.apn.contains(a)) {
			this.apn.add(a);
		}
	}

	public void addsmsemail(String sms) {
		if (this.smsemail == null) {
			this.smsemail = new ArrayList<String>();
		}
		if (!this.smsemail.contains(sms) && sms.length() > 0) {
			smsemail.add(sms);
		}
	}

	public void addmmsemail(String mms) {
		if (this.mmsemail == null) {
			this.mmsemail = new ArrayList<String>();
		}
		if (!this.mmsemail.contains(mms) && mms.length() > 0) {
			mmsemail.add(mms);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof APN) {
			MccMnc m = (MccMnc) o;
			return this.apn.equals(m.apn) && this.operator.equals(m.operator)
					&& this.smsemail.equals(m.smsemail) && this.mcc == m.mcc
					&& this.mnc == m.mnc;
		}
		return false;
	}

	@Override
	public int hashCode() {

		return new String(apn.toString() + mcc + "" + mnc + operator.toString())
				.hashCode();
	}

	@Override
	public String toString() {

		return mcc + "  " + mnc + "  " + iso + "  " + operator + "  "
				+ smsemail + "  " + mmsemail + "  " + apn;
	}

}

