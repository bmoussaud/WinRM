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

import org.bridj.Pointer;
import org.junit.Test;


public class GssapiLibrayTest {
	@Test
	public void importName() {

		Pointer<Integer> minstat = Pointer.allocateInt();
		gss_buffer_desc buffer = new gss_buffer_desc();
		final String service = "HTTP/WIN-2MGY3RY6XSH.deployit.local";
		buffer.value(Pointer.pointerToCString(service));
		buffer.length(service.length());

		Pointer<gss_buffer_desc> desc = Pointer.pointerTo(buffer);
		Pointer<gss_OID_desc> mech = Pointer.pointerTo(new gss_OID_desc());

		final Pointer<GssapiLibrary.gss_name_t_desc_struct> initSvcNamePtr = Pointer.allocateTypedPointer(GssapiLibrary.gss_name_t_desc_struct.class);
		final int i = GssapiLibrary.gss_import_name(minstat, desc, mech, initSvcNamePtr);
		System.out.println("ret = " + i);
		System.out.println("min = " + minstat.get());
		System.out.println("min = " + initSvcNamePtr);

		final GssapiLibrary.gss_name_t_desc_struct initSvcName = initSvcNamePtr.get();
		System.out.println("initSvcName = " + initSvcName);


		Pointer<GssapiLibrary.gss_ctx_id_t_desc_struct> gss_ctx_id_tPtr1 = Pointer.allocateTypedPointer(GssapiLibrary.gss_ctx_id_t_desc_struct.class);
		int flags = (GssapiLibrary.GSS_C_MUTUAL_FLAG | GssapiLibrary.GSS_C_SEQUENCE_FLAG | GssapiLibrary.GSS_C_CONF_FLAG | GssapiLibrary.GSS_C_INTEG_FLAG);
		Pointer<gss_buffer_desc> in_tok = Pointer.pointerTo(new gss_buffer_desc());
		Pointer<gss_buffer_desc> out_tok = Pointer.allocate(gss_buffer_desc.class);
		Pointer<Integer> ret_flags = Pointer.allocateInt();
		Pointer<Integer> time_rec = Pointer.allocateInt();

		final int retInitSecContext = GssapiLibrary.gss_init_sec_context(minstat,
				(GssapiLibrary.gss_cred_id_t_desc_struct) Pointer.NULL,
				gss_ctx_id_tPtr1,
				initSvcName,
				mech,
				flags,
				0,
				Pointer.NULL,
				in_tok,
				Pointer.NULL,
				out_tok,
				ret_flags, time_rec);


		System.out.println("retInitSecContext = " + retInitSecContext);
		System.out.println("retInitSecContext GssError = " + GssErrors.GssError(retInitSecContext));
		System.out.println("retInitSecContext GssSupInfo = " + GssErrors.GssSupInfo(retInitSecContext));
		System.out.println("retInitSecContext GssRoutingError = " + GssErrors.GssRoutingError(retInitSecContext));
		System.out.println("retInitSecContext GssCallingError = " + GssErrors.GssCallingError(retInitSecContext));

		Pointer<Integer> messageContext = Pointer.allocateInt();
		Pointer<gss_buffer_desc> statusString = Pointer.pointerTo(new gss_buffer_desc());
		int retStatus = GssapiLibrary.gss_display_status(minstat,
				retInitSecContext,
				GssapiLibrary.GSS_C_GSS_CODE,
				Pointer.pointerTo(new gss_OID_desc()),
				messageContext,
				statusString);

		final long length = statusString.get().length();
		final Pointer<Byte> as = statusString.get().value().as(Byte.class);
		final String cString = as.getCString();
	   	System.out.println("mc "+messageContext.get());
		System.out.println("s "+cString);
		if (GssErrors.isCallingError(retInitSecContext)) {

		}


		final String hex = Integer.toHexString(retInitSecContext);
		System.out.println("hex = " + hex);
		System.out.println("hex = " + String.format("%16X", retInitSecContext));


		System.out.println("min = " + minstat.get());
		System.out.println("out_tok = " + out_tok);
		System.out.println("time_rec = " + time_rec);

	}
}
