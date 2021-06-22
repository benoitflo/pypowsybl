/**
 * Copyright (c) 2021, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.python;

import com.powsybl.iidm.network.test.EurostagTutorialExample1Factory;
import com.powsybl.iidm.network.test.ShuntTestCaseFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Yichen TANG <yichen.tang at rte-france.com>
 */
class CreateEquipmentHelperTest {

    @Test
    void generator() {
        var network = EurostagTutorialExample1Factory.create();
        Map<String, Double> doubleMap = Map.ofEntries(
                entry("max_p", 3.0d),
                entry("min_p", 1.0d),
                entry("target_p", 4.0d),
                entry("target_v", 6.0d),
                entry("target_q", 7.0d),
                entry("rated_s", 5.0d));
        Map<String, String> strMap = Map.ofEntries(
                entry("voltage_level_id", "VLGEN"),
                entry("connectable_bus_id", "NGEN"),
                entry("bus_id", "NGEN"));
        Map<String, Integer> intMap = Map.of("voltage_regulator_on", 1);
        CreateEquipmentHelper.createElement(PyPowsyblApiHeader.ElementType.GENERATOR, network, "test", doubleMap, strMap, intMap);
        assertEquals(2, network.getGeneratorCount());
    }

    @Test
    void shunt() {
        var network = ShuntTestCaseFactory.create();
        Map<String, Double> doubleMap = Map.ofEntries(
                entry("b", 1.0),
                entry("g", 2.0),
                entry("target_v", 30.0),
                entry("target_deadband", 4.0)
        );
        Map<String, Integer> ints = Map.ofEntries(
                entry("section_count", 4)
        );
        Map<String, String> strMap = Map.ofEntries(
                entry("voltage_level_id", "VL1"),
                entry("connectable_bus_id", "B1"),
                entry("bus_id", "B1"));
        CreateEquipmentHelper.createElement(PyPowsyblApiHeader.ElementType.SHUNT_COMPENSATOR,
                network, "SHUNT2", doubleMap, strMap, ints);
        assertEquals(2, network.getShuntCompensatorCount());
    }
}