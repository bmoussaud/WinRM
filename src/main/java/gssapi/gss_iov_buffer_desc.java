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
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
/**
 * <i>native declaration : src/header/gssapi/gssapi.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("gssapi") 
public class gss_iov_buffer_desc extends StructObject {
	public gss_iov_buffer_desc() {
		super();
	}
	public gss_iov_buffer_desc(Pointer pointer) {
		super(pointer);
	}
	/// C type : OM_uint32
	@Field(0) 
	public int type() {
		return this.io.getIntField(this, 0);
	}
	/// C type : OM_uint32
	@Field(0) 
	public gss_iov_buffer_desc type(int type) {
		this.io.setIntField(this, 0, type);
		return this;
	}
	/// C type : gss_buffer_desc
	@Field(1) 
	public gss_buffer_desc buffer() {
		return this.io.getNativeObjectField(this, 1);
	}
}
