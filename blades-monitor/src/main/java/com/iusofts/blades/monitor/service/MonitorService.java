package com.iusofts.blades.monitor.service;

import com.iusofts.blades.common.domain.ServiceInstanceDetail;
import com.iusofts.blades.monitor.inft.MonitorInterface;
import com.iusofts.blades.monitor.inft.dto.Dependency;
import com.iusofts.blades.monitor.inft.dto.Edge;
import com.iusofts.blades.monitor.inft.dto.Node;
import com.iusofts.blades.monitor.inft.dto.OverviewCount;
import com.iusofts.blades.monitor.inft.enums.ApplicationCallCountType;
import com.iusofts.blades.monitor.service.dao.MonitorRecordDao;
import com.iusofts.blades.monitor.service.model.ApplicationCount;
import com.iusofts.blades.monitor.service.model.ApplicationRelation;
import com.iusofts.blades.monitor.service.model.MonitorRecord;
import com.iusofts.blades.monitor.web.vo.MonitorRecordVo;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 监控服务
 */
@Service
public class MonitorService implements MonitorInterface {

    @Autowired
    private MonitorRecordDao monitorRecordDao;
    @Autowired
    private ManageService manageService;

    @Override
    public void monitor(MonitorRecordVo recordVo) {
        if (recordVo != null) {
            MonitorRecord record = new MonitorRecord();
            BeanUtils.copyProperties(recordVo, record);
            monitorRecordDao.add(record);
        }
    }

    @Override
    public List<ApplicationCount> getAllApplicationCount(ApplicationCallCountType type) {
        List<ApplicationCount> countList = this.monitorRecordDao.getAllProviderAndCallCount(type.getName());
        for (ApplicationCount count : countList) {
            count.setUnitCountList(this.monitorRecordDao.getProviderCallCountByMinute(count.getAppName(), type.getName(), type.getGroupBy()));
        }
        return countList;
    }

    @Override
    public OverviewCount overviewCount() {
        OverviewCount count = new OverviewCount();
        count.setServiceCount(manageService.getServiceList().size());
        count.setAppCount(manageService.getApplicationList().size());

        Set<String> providerSet = new HashSet<>();
        for (ServiceInstance<ServiceInstanceDetail> instance : manageService.getServiceDataList()) {
            providerSet.add(instance.getAddress() + ":" + instance.getPort());
        }
        count.setProviderCount(providerSet.size());

        //count.setConsumerCount(4);
        //count.setErrorCount(5);
        //count.setWarningCount(6);
        return count;
    }

    @Override
    public Dependency getApplicationDependency() {
        Dependency dependency = new Dependency();
        Set<String> nodeSet = new HashSet<>();
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<ApplicationRelation> relations = this.monitorRecordDao.getApplicationRelations();

        for (ApplicationRelation relation : relations) {
            nodeSet.add(relation.getConsumerName());
            nodeSet.add(relation.getProviderName());
            Edge edge = new Edge();
            edge.setFromID(relation.getConsumerName());
            edge.setRelation("调用" + relation.getCount() + "次");
            edge.setToID(relation.getProviderName());
            edge.setWeight(1);
            edges.add(edge);
        }

        for (String s : nodeSet) {
            Node node = new Node();
            node.setIcon("APP");
            node.setId(s);
            node.setName(s);
            nodes.add(node);
        }

        dependency.setNodes(nodes);
        dependency.setEdges(edges);
        return dependency;
    }
}
