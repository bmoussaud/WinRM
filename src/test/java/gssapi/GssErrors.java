/*
 * This file is part of WinRM.
 *
 * WinRM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WinRM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WinRM.  If not, see <http://www.gnu.org/licenses/>.
 */
package gssapi;

import org.junit.Test;

import java.math.BigInteger;

/**
 * http://download.oracle.com/docs/cd/E19082-01/819-2145/reference-5/index.html
 */
public class GssErrors {
	public static final int GSS_C_SUPPLEMENTARY_OFFSET = 0;
	public static final int GSS_C_ROUTINE_ERROR_OFFSET = 16;
	public static final int GSS_C_CALLING_ERROR_OFFSET = 24;
	public static final int GSS_C_CALLING_ERROR_MASK = 255;
	public static final int GSS_C_ROUTINE_ERROR_MASK = 255;
	public static final int GSS_C_SUPPLEMENTARY_MASK = 65535;

	public static BigInteger XGSS_C_CALLING_ERROR_MASK = new BigInteger("0377");

	public static final int GSS_S_BAD_MECH = (1 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_BAD_NAME = (2 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_BAD_NAMETYPE = (3 << GSS_C_ROUTINE_ERROR_OFFSET);

	public static final int GSS_S_BAD_BINDINGS = (4 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_BAD_STATUS = (5 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_BAD_SIG = (6 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_BAD_MIC = GSS_S_BAD_SIG;
	public static final int GSS_S_NO_CRED = (7 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_NO_CONTEXT = (8 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_DEFECTIVE_TOKEN = (9 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_DEFECTIVE_CREDENTIAL = (10 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_CREDENTIALS_EXPIRED = (11 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_CONTEXT_EXPIRED = (12 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_FAILURE = (13 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_BAD_QOP = (14 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_UNAUTHORIZED = (15 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_UNAVAILABLE = (16 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_DUPLICATE_ELEMENT = (17 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_NAME_NOT_MN = (18 << GSS_C_ROUTINE_ERROR_OFFSET);
	public static final int GSS_S_BAD_MECH_ATTR = (19 << GSS_C_ROUTINE_ERROR_OFFSET);


	public static int GssCallingError(int x) {
		return (x & (GSS_C_CALLING_ERROR_MASK << GSS_C_CALLING_ERROR_OFFSET));
	}

	public static int GssRoutingError(int x) {
		return (x & (GSS_C_ROUTINE_ERROR_MASK << GSS_C_ROUTINE_ERROR_OFFSET));
	}

	public static int GssSupInfo(int x) {
		return (x & (GSS_C_SUPPLEMENTARY_MASK << GSS_C_SUPPLEMENTARY_OFFSET));
	}


	public static int GssError(int x) {
		return (x & ((GSS_C_CALLING_ERROR_MASK << GSS_C_CALLING_ERROR_OFFSET) | (GSS_C_ROUTINE_ERROR_MASK << GSS_C_ROUTINE_ERROR_OFFSET)));
	}

	public static boolean isCallingError(int code) {
		return GssCallingError(code) == 0;
	}

	@Test
	public void x() {
		BigInteger GSS_C_CALLING_ERROR_MASK = new BigInteger("0377");
		BigInteger GSS_C_CALLING_ERROR_OFFSET = new BigInteger("24");
		System.out.println("GSS_C_CALLING_ERROR_MASK = " + GSS_C_CALLING_ERROR_MASK);
		System.out.println("GSS_C_CALLING_ERROR_OFFSET = " + GSS_C_CALLING_ERROR_OFFSET);
	}
}
