package com.powsybl.python.dynamic;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.powsybl.dynamicsimulation.DynamicModel;
import com.powsybl.dynamicsimulation.DynamicModelsSupplier;
import com.powsybl.dynawaltz.dynamicmodels.GeneratorSynchronousFourWindings;
import com.powsybl.dynawaltz.dynamicmodels.GeneratorSynchronousFourWindingsProportionalRegulations;
import com.powsybl.dynawaltz.dynamicmodels.GeneratorSynchronousThreeWindings;
import com.powsybl.dynawaltz.dynamicmodels.GeneratorSynchronousThreeWindingsProportionalRegulations;
import com.powsybl.dynawaltz.dynamicmodels.LoadAlphaBeta;
import com.powsybl.dynawaltz.dynamicmodels.LoadOneTransformer;
import com.powsybl.dynawaltz.dynamicmodels.OmegaRef;
import com.powsybl.iidm.network.Network;

public class DynamicModelMapper implements DynamicModelsSupplier {

    private LinkedList<Supplier<DynamicModel>> dynamicModelList;

    public DynamicModelMapper() {
        dynamicModelList = new LinkedList<>();
    }

    public List<DynamicModel> get(Network network) {
        return dynamicModelList.stream().map(supplier -> supplier.get()).collect(Collectors.toList());
    }

    /**
     * maps static element to a dynamic alphabeta load
     *
     * @param staticId also determines the dynamic id of the element
     */
    public void addAlphaBetaLoad(String staticId, String parametersIds) {
        dynamicModelList.add(() -> new LoadAlphaBeta(staticId, staticId, parametersIds));
    }

    /**
     * maps static element to a dynamic one transformer
     *
     * @param staticId also determines the dynamic id of the element
     */
    public void addOneTransformerLoad(String staticId, String parametersIds) {
        dynamicModelList.add(() -> new LoadOneTransformer(staticId, staticId, parametersIds));
    }

    public void addOmegaRef(String generatorId) {
        dynamicModelList.add(() -> new OmegaRef(generatorId));
    }

    public void addGeneratorSynchronousThreeWindings(String staticId, String parametersIds) {
        dynamicModelList.add(() -> new GeneratorSynchronousThreeWindings(staticId, staticId, parametersIds));
    }

    public void addGeneratorSynchronousThreeWindingsProportionalRegulations(String staticId, String parametersIds) {
        dynamicModelList.add(
                () -> new GeneratorSynchronousThreeWindingsProportionalRegulations(staticId, staticId, parametersIds));
    }

    public void addGeneratorSynchronousFourWindings(String staticId, String parametersIds) {
        dynamicModelList.add(() -> new GeneratorSynchronousFourWindings(staticId, staticId, parametersIds));
    }

    public void addGeneratorSynchronousFourWindingsProportionalRegulations(String staticId, String parametersIds) {
        dynamicModelList.add(
                () -> new GeneratorSynchronousFourWindingsProportionalRegulations(staticId, staticId, parametersIds));
    }

}