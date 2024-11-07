/*
 ***************************************************************************************
 *  Copyright (C) 2024 VU, Inc. All rights reserved.                                   *
 *  https://www.vusecurity.com/                                                        *
 *  ---------------------------------------------------------------------------------- *
 *  This program is free software; you can redistribute it and/or modify               *
 *  it under the terms of the GNU General Public License version 2                     *
 *  as published by the Free Software Foundation.                                      *
 *                                                                                     *
 *  This program is distributed in the hope that it will be useful,                    *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of                     *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      *
 *  GNU General Public License for more details.                                       *
 *                                                                                     *
 *  You should have received a copy of the GNU General Public License along            *
 *  with this program; if not, write to the Free Software Foundation, Inc.,            *
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.                        *
 ***************************************************************************************
 */

package com.vusecurity.eventprocessing.services;

import com.espertech.esper.common.client.serde.DataInputOutputSerde;
import com.espertech.esper.common.client.serde.EventBeanCollatedWriter;
import com.vusecurity.eventprocessing.models.Event;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventSerializerDeserializer implements DataInputOutputSerde<Event> {
    @Override
    public void write(Event event, DataOutput dataOutput, byte[] bytes, EventBeanCollatedWriter eventBeanCollatedWriter) throws IOException {
        writeEvent(event, dataOutput);
    }

    private void writeEvent(Event event, DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(event.getId());
        dataOutput.writeInt(event.getIdChannel());
        dataOutput.writeInt(event.getIdOperation());
        dataOutput.writeUTF(event.getUserId());
        dataOutput.writeLong(event.getEventDate().getTime());

        writeAdditionalParameters(event.getAditionalParameters(), dataOutput);

    }

    private void writeAdditionalParameters(Map<String, String> aditionalParameters, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(aditionalParameters.size());

        for (Map.Entry<String, String> entry: aditionalParameters.entrySet()) {
            dataOutput.writeUTF(entry.getKey());
            dataOutput.writeUTF(entry.getValue());
        }
    }

    @Override
    public Event read(DataInput dataInput, byte[] bytes) throws IOException {

        Event event = new Event();
        event.setId(dataInput.readLong());
        event.setIdChannel(dataInput.readInt());
        event.setIdOperation(dataInput.readInt());
        event.setUserId(dataInput.readUTF());
        event.setEventDate(new Date(dataInput.readLong()));

        readAdditionalParameters(event, dataInput);

        return event;
    }

    private void readAdditionalParameters(Event event, DataInput dataInput) throws IOException {

        int size = dataInput.readInt();

        Map<String, String> additionalParameters = new HashMap<>(size);

        for (int i = 0; i < size; i++){
            additionalParameters.put(dataInput.readUTF(), dataInput.readUTF());
        }

        event.setAditionalParameters(additionalParameters);
    }
}
