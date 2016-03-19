/*
 * This file is part of j2mod.
 *
 * j2mod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * j2mod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with j2mod.  If not, see <http://www.gnu.org/licenses
 */
package com.j2mod.modbus.msg;

import com.j2mod.modbus.Modbus;

/**
 * @author Julie
 * @version @version@ (@date@)
 */
public class IllegalAddressExceptionResponse extends ExceptionResponse {

    /**
     *
     */
    public IllegalAddressExceptionResponse() {
        super(0, Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }

    public IllegalAddressExceptionResponse(int function) {
        super(function, Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }

    /**
     *
     */
    public void setFunctionCode(int fc) {
        super.setFunctionCode(fc | Modbus.EXCEPTION_OFFSET);
    }
}
