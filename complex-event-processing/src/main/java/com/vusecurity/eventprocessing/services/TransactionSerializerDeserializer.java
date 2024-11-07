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
import com.vusecurity.eventprocessing.models.Transaction;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransactionSerializerDeserializer implements DataInputOutputSerde<Transaction> {

    @Override
    public void write(Transaction transaction, DataOutput dataOutput, byte[] bytes, EventBeanCollatedWriter eventBeanCollatedWriter) throws IOException {
        writeTransaction(transaction, dataOutput);
    }

    private void writeTransaction(Transaction transaction, DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(transaction.getId());
        dataOutput.writeInt(transaction.getIdChannel());
        dataOutput.writeInt(transaction.getIdOperation());
        dataOutput.writeUTF(transaction.getUserId());
        dataOutput.writeLong(transaction.getEventDate().getTime());
        dataOutput.writeDouble(transaction.getAmount());
        dataOutput.writeUTF(transaction.getCreditAccount());
        dataOutput.writeUTF(transaction.getDebitAccount());

        writeAdditionalParameters(transaction.getAditionalParameters(), dataOutput);
    }

    private void writeAdditionalParameters(Map<String, String> aditionalParameters, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(aditionalParameters.size());

        for (Map.Entry<String, String> entry: aditionalParameters.entrySet()) {
            dataOutput.writeUTF(entry.getKey());
            dataOutput.writeUTF(entry.getValue());
        }
    }

    @Override
    public Transaction read(DataInput dataInput, byte[] bytes) throws IOException {
        Transaction transaction = new Transaction();
        transaction.setId(dataInput.readLong());
        transaction.setIdChannel(dataInput.readInt());
        transaction.setIdOperation(dataInput.readInt());
        transaction.setUserId(dataInput.readUTF());
        transaction.setEventDate(new Date(dataInput.readLong()));
        transaction.setAmount(dataInput.readDouble());
        transaction.setCreditAccount(dataInput.readUTF());
        transaction.setDebitAccount(dataInput.readUTF());

        readAdditionalParameters(transaction, dataInput);

        return transaction;
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
