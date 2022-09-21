import pypowsybl._pypowsybl as _pp
import networkx as _nx
from pandas import DataFrame
from pypowsybl.util import create_data_frame_from_series_array as _create_data_frame_from_series_array



class NodeBreakerTopology:
    """
    Node-breaker representation of the topology of a voltage level.

    The topology is actually represented as a graph, where
    vertices are called "nodes" and are identified by a unique number in the voltage level,
    while edges are switches (breakers and disconnectors), or internal connections (plain "wires").
    """

    def __init__(self, network_handle: _pp.JavaHandle, voltage_level_id: str):
        self._internal_connections = _create_data_frame_from_series_array(
            _pp.get_node_breaker_view_internal_connections(network_handle, voltage_level_id))
        self._switchs = _create_data_frame_from_series_array(
            _pp.get_node_breaker_view_switches(network_handle, voltage_level_id))
        self._nodes = _create_data_frame_from_series_array(
            _pp.get_node_breaker_view_nodes(network_handle, voltage_level_id))

    @property
    def switches(self) -> DataFrame:
        """
        The list of switches of the voltage level, together with their connection status, as a dataframe.
        """
        return self._switchs

    @property
    def nodes(self) -> DataFrame:
        """
        The list of nodes of the voltage level, together with their corresponding network element (if any),
        as a dataframe.
        """
        return self._nodes

    @property
    def internal_connections(self) -> DataFrame:
        """
        The list of internal connection of the voltage level, together with the nodes they connect.
        """
        return self._internal_connections

    def create_graph(self) -> _nx.Graph:
        """
        Representation of the topology as a networkx graph.
        """
        graph = _nx.Graph()
        graph.add_nodes_from(self._nodes.index.tolist())
        graph.add_edges_from(self._switchs[['node1', 'node2']].values.tolist())
        graph.add_edges_from(self._internal_connections[['node1', 'node2']].values.tolist())
        return graph