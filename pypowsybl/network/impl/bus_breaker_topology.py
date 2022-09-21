import networkx as _nx
import pypowsybl._pypowsybl as _pp
from pypowsybl.util import create_data_frame_from_series_array as _create_data_frame_from_series_array
from pandas import DataFrame

class BusBreakerTopology:
    """
    Bus-breaker representation of the topology of a voltage level.

    The topology is actually represented as a graph, where
    vertices are buses while edges are switches (breakers and disconnectors).

    For each element of the voltage level, we also provide the bus breaker bus where it is connected.
    """

    def __init__(self, network_handle: _pp.JavaHandle, voltage_level_id: str):
        self._elements = _create_data_frame_from_series_array(
            _pp.get_bus_breaker_view_elements(network_handle, voltage_level_id))
        self._switchs = _create_data_frame_from_series_array(
            _pp.get_bus_breaker_view_switches(network_handle, voltage_level_id))
        self._buses = _create_data_frame_from_series_array(
            _pp.get_bus_breaker_view_buses(network_handle, voltage_level_id))

    @property
    def switches(self) -> DataFrame:
        """
        The list of switches of the bus breaker view, together with their connection status, as a dataframe.
        """
        return self._switchs

    @property
    def buses(self) -> DataFrame:
        """
        The list of buses of the  bus breaker view, as a dataframe.
        """
        return self._buses

    @property
    def elements(self) -> DataFrame:
        """
        The list of elements (lines, generators...) of this voltage level, together with the bus
        of the bus breaker view where they are connected.
        """
        return self._elements

    def create_graph(self) -> _nx.Graph:
        """
        Representation of the topology as a networkx graph.
        """
        graph = _nx.Graph()
        graph.add_nodes_from(self._buses.index.tolist())
        graph.add_edges_from(self._switchs[['bus1_id', 'bus2_id']].values.tolist())
        return graph