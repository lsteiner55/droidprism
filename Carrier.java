package com.droidprism;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Carrier class for accessing sms gateways, APN settings, operator names, 
 *   mms gateways, iso country code, mcc, and mnc.
 *   
 * Carrier is accessible by searching mcc and mnc, or from iso country code and operator name.
 *
 * 
 *
 */
public class Carrier {

	private static Map<ArrayList<Integer>, MccMnc> map2 = null;
	private static Map<String, ArrayList<MccMnc>> map3 = null;

	private MccMnc m;

	private Carrier() {

	}

	/**
	 * 
	 * @return the apns
	 * 
	 */
	public List<APN> getallAPNs() {
		List<APN> a = new ArrayList<APN>();
		if (m.apn.size() > 0) {
			for (APN a2 : m.apn) {
				a.add(new APN(a2));
			}
		}

		return a;
	}

	/**
	 * Gets the apn.
	 * 
	 * @return the apn, or null if there is no apn
	 * 
	 */
	public APN getAPN() {
		if (m.apn.size() > 0) {
			return new APN(m.apn.get(0));
		}
		return null;
	}

	public String getisoCountryCode() {
		return m.iso;
	}

	public int getMcc() {
		return m.mcc;
	}

	public int getMnc() {
		return m.mnc;
	}

	/**
	 * Gets all possible sms emails. Prepend the phone number and @ symbol
	 * before the return value
	 * 
	 * @return list of email addresses of the sms gateways. Example:
	 *         smsgateway.com
	 * 
	 * 
	 */
	public List<String> getallsmsemails() {
		return new ArrayList<String>(m.smsemail);
	}

	/**
	 * Gets the sms email. Prepend the phone number and @ symbol before the
	 * return value
	 * 
	 * @return the email address of the sms gateway, or null if there is no sms
	 *         email gateway Example: smsgateway.com
	 * 
	 */
	public String getsmsemail() {
		if (m.smsemail.size() > 0) {
			return m.smsemail.get(0);
		}
		return "";
	}

	/**
	 * Gets all possible operators.
	 * 
	 * @return list of all operators for the same mcc mnc
	 * 
	 * 
	 */
	public List<String> getallOperators() {
		return new ArrayList<String>(m.operator);
	}

	/**
	 * Gets the operator
	 * 
	 * @return the operator
	 * 
	 */
	public String getOperator() {
		if (m.operator.size() > 0) {
			return m.operator.get(0);
		}
		return "";
	}

	/**
	 * Gets all possible mms emails. Prepend the phone number and @ symbol
	 * before the return value
	 * 
	 * @return list of email addresses of the mms gateways. Example:
	 *         mmsgateway.com
	 * 
	 * 
	 */
	public List<String> getallmmsemails() {
		return new ArrayList<String>(m.smsemail);
	}

	/**
	 * Gets the mms email. Prepend the phone number and @ symbol before the
	 * return value
	 * 
	 * @return the email address of the mms gateway, or null if there is no mms
	 *         email gateway Example: mmsgateway.com
	 * 
	 */
	public String getmmsemail() {
		if (m.mmsemail.size() > 0) {
			return m.mmsemail.get(0);
		}
		return null;
	}

	/**
	 * get carrier
	 * @param iso country code (Example: US GB or INT for international)
	 * @param carrier (Example: verizon, t-mobile)
	 * @return 
	 */
	public static Carrier getCarrier(String iso, String carrier) {
		try {
			initialize();
			if (iso.length() < 2 || iso.length() > 3) {
				return null;
			}
			Carrier c = new Carrier();
			ArrayList<MccMnc> a = map3.get(iso.toUpperCase());
			int i = 0;
			for (MccMnc a2 : a) {
				int i2 = 0;
				for (String operator : a2.operator) {
					if (operator.toLowerCase().contains(
							carrier.toLowerCase().trim())) {
						i2 += 3;
					} else if (carrier.toLowerCase().trim()
							.contains(operator.toLowerCase().trim())) {
						i2 += 1;
					}
				}
				if (i2 > i) {
					c.m = a2;
					i = i2;
				}
			}
			if (c.m == null) {
				return null;
			}
			return c;
		} catch (Exception ex) {
			return null;
		}
	}

	
	/**
	 * get carrier
	 * @param mcc mobile country code
	 * @param mnc mobile network code
	 * @return
	 */
	public static Carrier getCarrier(int mcc, int mnc) {

		try {
			initialize();
			ArrayList<Integer> a = new ArrayList<Integer>();
			a.add(mcc);
			a.add(mnc);
			MccMnc m = map2.get(a);
			if (m == null) {
				return null;
			}
			Carrier c = new Carrier();
			c.m = m;
			return c;
		} catch (Exception ex) {
			return null;
		}
	}

	private static Map<ArrayList<Integer>, MccMnc> read1(String file)
			throws Exception {
		Map<ArrayList<Integer>, MccMnc> m = new HashMap<ArrayList<Integer>, MccMnc>();
		String s3 = "";
		InputStream is = null;
		try {
			StringBuffer sb = new StringBuffer();
			is = new Carrier().getClass().getResourceAsStream(file);
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = is.read(b)) != -1) {
				sb.append(new String(b, 0, i, "UTF-8"));
			}
			s3 = sb.toString();
		} finally {
			is.close();
		}
		Document d = Jsoup.parse(s3);
		Elements a = d.getElementsByTag("a");
		for (Iterator<Element> i = a.iterator(); i.hasNext();) {
			Element h = i.next();
			ArrayList<Integer> t = new ArrayList<Integer>();
			int mcc = Integer.parseInt(h.attr("mcc"));
			int mnc = Integer.parseInt(h.attr("mnc"));
			t.add(mcc);
			t.add(mnc);
			String iso = h.attr("iso");
			MccMnc m2 = new MccMnc(mcc, mnc, iso, "");
			Elements carrier = h.getElementsByTag("carrier");
			for (Iterator<Element> i2 = carrier.iterator(); i2.hasNext();) {
				Element h2 = i2.next();
				m2.addoperator(h2.text().trim());
			}
			Elements s = h.getElementsByTag("sms");
			for (Iterator<Element> i2 = s.iterator(); i2.hasNext();) {
				Element h2 = i2.next();
				m2.addsmsemail(h2.text().trim());
			}
			Elements mm = h.getElementsByTag("mms");
			for (Iterator<Element> i2 = mm.iterator(); i2.hasNext();) {
				Element h2 = i2.next();
				m2.addmmsemail(h2.text().trim());
			}
			Elements g = h.getElementsByTag("apn");
			for (Iterator<Element> i2 = g.iterator(); i2.hasNext();) {
				Element h2 = i2.next();
				String o = h2.attr("mmsport");
				int ii = o.matches("\\d{1,6}") ? Integer.parseInt(o) : 0;
				APN n = new APN(h2.attr("mmsc"), h2.attr("mmsproxy"), ii,
						h2.attr("name"));
				n.user = h2.attr("user");
				n.password = h2.attr("password");
				m2.addapn(n);
			}
			m.put(t, m2);
		}
		return m;
	}

	private static Map<String, ArrayList<MccMnc>> toiso(
			Map<ArrayList<Integer>, MccMnc> m) {

		Map<String, ArrayList<MccMnc>> m2 = new HashMap<String, ArrayList<MccMnc>>();
		for (Map.Entry<ArrayList<Integer>, MccMnc> m3 : m.entrySet()) {
			ArrayList<Integer> aa = m3.getKey();
			MccMnc a3 = m3.getValue();

			ArrayList<MccMnc> aaa = m2.get(a3.iso);
			if (aaa == null) {
				aaa = new ArrayList<MccMnc>();
			}

			aaa.add(a3);

			m2.put(a3.iso, aaa);
		}

		return m2;

	}

	private static synchronized void initialize() throws Exception {
		if (map2 == null) {
			map2 = read1("v.txt");
			map3 = toiso(map2);

		}
	}
}
