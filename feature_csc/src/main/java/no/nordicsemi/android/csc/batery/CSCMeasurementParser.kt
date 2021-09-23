/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package no.nordicsemi.android.csc.batery

import no.nordicsemi.android.ble.data.Data

object CSCMeasurementParser {

    private const val WHEEL_REV_DATA_PRESENT: Byte = 0x01 // 1 bit
    private const val CRANK_REV_DATA_PRESENT: Byte = 0x02 // 1 bit

    @JvmStatic
	fun parse(data: Data): String {
        var offset = 0
        val flags = data.getByte(offset)!!.toInt() // 1 byte
        offset += 1
        val wheelRevPresent = flags and WHEEL_REV_DATA_PRESENT.toInt() > 0
        val crankRevPreset = flags and CRANK_REV_DATA_PRESENT.toInt() > 0
        var wheelRevolutions = 0
        var lastWheelEventTime = 0
        if (wheelRevPresent) {
            wheelRevolutions = data.getIntValue(Data.FORMAT_UINT32, offset)!!
            offset += 4
            lastWheelEventTime = data.getIntValue(Data.FORMAT_UINT16, offset)!! // 1/1024 s
            offset += 2
        }
        var crankRevolutions = 0
        var lastCrankEventTime = 0
        if (crankRevPreset) {
            crankRevolutions = data.getIntValue(Data.FORMAT_UINT16, offset)!!
            offset += 2
            lastCrankEventTime = data.getIntValue(Data.FORMAT_UINT16, offset)!!
            //offset += 2;
        }
        val builder = StringBuilder()
        if (wheelRevPresent) {
            builder.append("Wheel rev: ").append(wheelRevolutions).append(",\n")
            builder.append("Last wheel event time: ").append(lastWheelEventTime).append(",\n")
        }
        if (crankRevPreset) {
            builder.append("Crank rev: ").append(crankRevolutions).append(",\n")
            builder.append("Last crank event time: ").append(lastCrankEventTime).append(",\n")
        }
        if (!wheelRevPresent && !crankRevPreset) {
            builder.append("No wheel or crank data")
        }
        builder.setLength(builder.length - 2)
        return builder.toString()
    }
}