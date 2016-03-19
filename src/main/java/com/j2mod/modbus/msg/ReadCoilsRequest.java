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
import com.j2mod.modbus.ModbusCoupler;
import com.j2mod.modbus.procimg.DigitalOut;
import com.j2mod.modbus.procimg.IllegalAddressException;
import com.j2mod.modbus.procimg.ProcessImage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>ReadCoilsRequest</tt>. The implementation directly
 * correlates with the class 1 function <i>read coils (FC 1)</i>. It
 * encapsulates the corresponding request message.
 *
 * <p>
 * Coils are understood as bits that can be manipulated (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @author jfhaugh
 * @version @version@ (@date@)
 */
public final class ReadCoilsRequest extends ModbusRequest {

    // instance attributes
    private int m_Reference;
    private int m_BitCount;

    /**
     * Constructs a new <tt>ReadCoilsRequest</tt> instance.
     */
    public ReadCoilsRequest() {
        super();

        setFunctionCode(Modbus.READ_COILS);
        setDataLength(4);
    }

    /**
     * Constructs a new <tt>ReadCoilsRequest</tt> instance with a given
     * reference and count of coils (i.e. bits) to be read.
     * <p>
     *
     * @param ref   the reference number of the register to read from.
     * @param count the number of bits to be read.
     */
    public ReadCoilsRequest(int ref, int count) {
        super();

        setFunctionCode(Modbus.READ_COILS);
        setDataLength(4);

        setReference(ref);
        setBitCount(count);
    }

    public ReadCoilsResponse getResponse() {
        ReadCoilsResponse response;
        response = new ReadCoilsResponse(m_BitCount);

        // transfer header data
        if (!isHeadless()) {
            response.setTransactionID(getTransactionID());
            response.setProtocolID(getProtocolID());
        }
        else {
            response.setHeadless();
        }
        response.setUnitID(getUnitID());

        return response;
    }

    public ModbusResponse createResponse() {
        ModbusResponse response;
        DigitalOut[] douts;

        // 1. get process image
        ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
        // 2. get input discretes range
        try {
            douts = procimg.getDigitalOutRange(getReference(), getBitCount());
        }
        catch (IllegalAddressException e) {
            response = new IllegalAddressExceptionResponse();
            response.setUnitID(getUnitID());
            response.setFunctionCode(getFunctionCode());

            return response;
        }
        response = getResponse();

		/*
         * Populate the discrete values from the process image.
		 */
        for (int i = 0; i < douts.length; i++) {
            ((ReadCoilsResponse)response).setCoilStatus(i, douts[i].isSet());
        }

        return response;
    }

    /**
     * Returns the reference of the register to to start reading from with this
     * <tt>ReadCoilsRequest</tt>.
     * <p>
     *
     * @return the reference of the register to start reading from as
     * <tt>int</tt>.
     */
    public int getReference() {
        return m_Reference;
    }

    /**
     * Sets the reference of the register to start reading from with this
     * <tt>ReadCoilsRequest</tt>.
     * <p>
     *
     * @param ref the reference of the register to start reading from.
     */
    public void setReference(int ref) {
        m_Reference = ref;
    }

    /**
     * Returns the number of bits (i.e. coils) to be read with this
     * <tt>ReadCoilsRequest</tt>.
     * <p>
     *
     * @return the number of bits to be read.
     */
    public int getBitCount() {
        return m_BitCount;
    }

    /**
     * Sets the number of bits (i.e. coils) to be read with this
     * <tt>ReadCoilsRequest</tt>.
     * <p>
     *
     * @param count the number of bits to be read.
     */
    public void setBitCount(int count) {
        if (count > Modbus.MAX_BITS) {
            throw new IllegalArgumentException("Maximum bitcount exceeded");
        }
        else {
            m_BitCount = count;
        }
    }

    public void writeData(DataOutput dout) throws IOException {
        dout.write(getMessage());
    }

    public void readData(DataInput din) throws IOException {
        m_Reference = din.readUnsignedShort();
        m_BitCount = din.readUnsignedShort();
    }

    public byte[] getMessage() {
        byte result[] = new byte[4];

        result[0] = (byte)((m_Reference >> 8) & 0xff);
        result[1] = (byte)((m_Reference & 0xff));
        result[2] = (byte)((m_BitCount >> 8) & 0xff);
        result[3] = (byte)((m_BitCount & 0xff));

        return result;
    }
}
