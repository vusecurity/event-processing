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

import com.espertech.esper.common.client.serde.SerdeProvider;
import com.espertech.esper.common.client.serde.SerdeProviderContextClass;
import com.espertech.esper.common.client.serde.SerdeProvision;
import com.espertech.esper.common.client.serde.SerdeProvisionByClass;
import com.vusecurity.eventprocessing.models.Event;
import com.vusecurity.eventprocessing.models.Transaction;

public class FraudSerdeProvider  implements SerdeProvider {

    protected final static FraudSerdeProvider INSTANCE = new FraudSerdeProvider();

    @Override
    public SerdeProvision resolveSerdeForClass(SerdeProviderContextClass serdeProviderContextClass) {
        if (serdeProviderContextClass.getClazz().getType() == Event.class) {
            return new SerdeProvisionByClass(EventSerializerDeserializer.class);
        }
        if (serdeProviderContextClass.getClazz().getType() == Transaction.class) {
            return new SerdeProvisionByClass(TransactionSerializerDeserializer.class);
        }
        return null;
    }
}
