		var nodeGraph = null;//root
        var choiceNode=null;//当前选择的顶点
        var choiceNode2=null;//当前选择的顶点
        var choiceNodeIndex=null;//当前选择的顶点
        var isOverNode=false;//鼠标是否在顶点上
        var nodes=null;//所有顶点数据
        var edges=null;//所有关系数据
        var edges_line=null;//获取关系连接线
        var edges_text=null;//获取关系名称
        var nodes_img=null;//获取图片节点
        var nodes_text=null;//获取图片节点名称
        //var rect = null;//凸显根节点
        var pathRoot = null;//凸显根节点
        var rootName = null;//根节点名称
        var rootId = null;//根节点唯一标识
        var rootLx = null;//根节点类型

        var drag=null;
        var node=null;
        var force=null;
        var label_text_1=null;
        var label_text_2=null;
        var node_start_x,node_start_y;
        var isIE  = false;

        //2015.9.26新增框选功能
        var shiftKey, ctrlKey;
        var nodeGraph = null;
        var xScale = d3.scale.linear()
        .domain([0,width]).range([0,width]);
        var yScale = d3.scale.linear()
        .domain([0,height]).range([0, height]);
        var translate=[];
        var scale=0;

        var hideRelations=[];//隐藏的关系
        var showRelations=[];//展示的关系


		//设置缩放
        var isChoiceNode=false;

		$("body").mouseup(function(){
			isChoiceNode=false;
		});
		//设置缩放、框选
        var zoomer = d3.behavior.zoom().
	        scaleExtent([0.1,10]).
	        on("zoomstart", zoomstart).
	        on("zoom", zoom);

		//创建SVG
		var svg = d3.select("#mysvg").attr("tabindex", 1)
									    .on("keydown.brush", keydown)
									    .on("keyup.brush", keyup)
									    .on("mouseup.brush", mouseup)
									    .each(function() { this.focus(); })
									    .append("svg")
										.attr("width",width)
										.attr("height",height);
		//铺上一层rect用于平移和缩放
		svg.append("rect")
	    .attr("class", "overlay")
	    .attr("width", width)
	    .attr("height", height);

		var defs = svg.append("defs");

		//结束箭头
		var arrowMarker = defs.append("marker")
								.attr("id","arrow")
								.attr("markerUnits","strokeWidth")
							    .attr("markerWidth","24")
		                        .attr("markerHeight","24")
		                        .attr("viewBox","0 0 24 24")
		                        .attr("refX","12")
		                        .attr("refY","6")
		                        .attr("orient","auto");

		var arrow_path = "M2,2 L10,6 L2,10 L6,6 L2,2";

		arrowMarker.append("path")
					.attr("d",arrow_path)
					.attr("fill","#000000");

		function zoomstart() {

		}



		//缩放触发事件
		function zoom() {
			if(!isChoiceNode){//当鼠标未点击顶点时
				$("#ttt01").text(d3.event.translate);
				$("#ttt02").text(d3.event.scale);
				translate=d3.event.translate;
				scale=d3.event.scale;
			  	vis.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
			  	//svg.attr("transform", "translate(" + [d3.event.translate[0]-(choiceNode2.x-node_start_x),d3.event.translate[1]-(choiceNode2.y-node_start_y)] + ")scale(" + d3.event.scale + ")");
			  	zoom_xy=d3.event.translate;
			}else{
				$("#ttt01").text(d3.event.translate);
				$("#ttt02").text(d3.event.scale);
				//纠正缩放位置
				zoomer.translate([d3.event.translate[0]-(choiceNode2.x-node_start_x)*d3.event.scale,d3.event.translate[1]-(choiceNode2.y-node_start_y)*d3.event.scale]);
	            zoomer.scale(d3.event.scale);
	            zoom_xy=[d3.event.translate[0]-(choiceNode2.x-node_start_x)*d3.event.scale,d3.event.translate[1]-(choiceNode2.y-node_start_y)*d3.event.scale];
			}
			zoom_num=d3.event.scale;
		}

		var brusher = d3.svg.brush()
	    //.x(d3.scale.identity().domain([0, width]))
	    //.y(d3.scale.identity().domain([0, height]))
	    .x(xScale)
	    .y(yScale)
	    .on("brushstart", function(d) {
	        //node.each(function(d) { d.previouslySelected = shiftKey && d.selected; });
	    })
	    .on("brush", function() {
	    	$("#ttt04").text(Math.random());
	        var extent = d3.event.target.extent();
			var b=0;
			var node=vis.selectAll(".nodetext");
	        node.classed("selected", function(d) {
	            return d.selected = d.previouslySelected ^
	            ((extent[0][0]-translate[0])/scale <= d.x && d.x < (extent[1][0]-translate[0])/scale
	             && (extent[0][1]-translate[1])/scale <= d.y && d.y < (extent[1][1]-translate[1])/scale);
	        });

	        var nodes_text = svg.selectAll(".nodetext")[0];
			for(var j=0;j<nodes_text.length;j++){
				if(nodes_text[j].__data__.selected == 1) {
					d3.select(nodes_text[j]).style("display","block");
				}
			}

	    })
	    .on("brushend", function() {
	        d3.event.target.clear();
	        d3.select(this).call(d3.event.target);
	    });

	    var svg_graph = svg.append('svg:g')
	    .call(zoomer)
	    //.call(brusher)

	    var rect = svg_graph.append('svg:rect')
	    .attr('width', width)
	    .attr('height', height)
	    .attr('fill', 'transparent')
	    //.attr('opacity', 0.5)
	    .attr('stroke', 'transparent')
	    .attr('stroke-width', 1)
	    //.attr("pointer-events", "all")
	    .attr("id", "zrect");

	    var brush = svg_graph.append("g")
	    .datum(function() { return {selected: false, previouslySelected: false}; })
	    .attr("class", "brush");

	    var vis = svg_graph.append("svg:g");

	    vis.attr('id', 'vis');


	    brush.call(brusher)
	    .on("mousedown.brush", null)
	    .on("touchstart.brush", null)
	    .on("touchmove.brush", null)
	    .on("touchend.brush", null);

	    brush.select('.background').style('cursor', 'auto');

	  //框选
		function keydown() {
        shiftKey = d3.event.shiftKey || d3.event.metaKey;
        ctrlKey = d3.event.ctrlKey;
        $("#ttt04").text(shiftKey);

        //console.log('d3.event', d3.event);
        if (d3.event.keyCode == 67) {   //the 'c' key
            center_view();
        } else if(d3.event.keyCode == 46) {
        	//删除框选的节点
        	var nodes_text = svg.selectAll(".nodetext")[0];//所有节点
        	for(var j=0;j<nodes_text.length;j++){
        		if(!nodes_text[j].__data__.hasOwnProperty("ys") && nodes_text[j].__data__.selected == 1) {
        			delNode(nodes_text[j].__data__.id);
        		} else if(nodes_text[j].__data__.hasOwnProperty("ys") && nodes_text[j].__data__.ys == '' && nodes_text[j].__data__.selected == 1) {
        			delNode(nodes_text[j].__data__.id);
				}
        	}
        }

        if (shiftKey) {
        	$(".brush").show()
            svg_graph.call(zoomer)
            .on("mousedown.zoom", null)
            .on("touchstart.zoom", null)
            .on("touchmove.zoom", null)
            .on("touchend.zoom", null);

            //svg_graph.on('zoom', null);
            vis.selectAll('g.gnode')
            .on('mousedown.drag', null);

            brush.select('.background').style('cursor', 'crosshair')
            brush.call(brusher);
        }
    }

    function keyup() {
        shiftKey = d3.event.shiftKey || d3.event.metaKey;
        ctrlKey = d3.event.ctrlKey;
        $("#ttt04").text(shiftKey);
        brush.call(brusher)
        .on("mousedown.brush", null)
        .on("touchstart.brush", null)
        .on("touchmove.brush", null)
        .on("touchend.brush", null);

        brush.select('.background').style('cursor', 'auto')
        svg_graph.call(zoomer);
    }

    function mouseup(){
    	$(".brush").hide()
    }

    var hoverIndex;

		//居中
		function center_view(){
			var x = -2500+$(window).width()/2;//+document.getElementById("vis").getBoundingClientRect().width/2;
			var y = -2500+$(window).height()/2-10;
			vis.attr("transform", "translate(" + x + ","+ y+")scale(1.0)");
			zoomer.translate([x,y]);
            zoomer.scale(1.0);
            translate=[x,y];
            scale=1;
		}

		//获得所有子节点
		var data01=new Array();
		function getAllChildNode(delNode){
			data01.push(delNode);

		}
		//删除节点
		function deleteNode(delNode){
			getAllChildNode(delNode);//获得子节点
			delNum+=data01.length;//赋值删除顶点数
			//删除节点图片
			var image_nodes=d3.selectAll("image")[0];
			for(var j=0;j<image_nodes.length;j++){
				var d=image_nodes[j];
				for(var i=0;i<data01.length;i++){
					if(d.hasOwnProperty("__data__")){
						if(d.__data__.id==data01[i].id){
	   						d3.select(d).remove();

	   						d3.select("#bs_"+d.__data__.id).remove();
	   						d3.select("#zdry_"+d.__data__.id).remove();

	   						//若删除的是根节点，则删除描边
	   						if(pathRoot != null && d.__data__.id == pathRoot.attr("id").substr(3)) {
	   							d3.select(".pathRoot").remove();
	   						}
	   					}
					}
   				}
			}
			//清除被删的节点数据
			for(var j=0;j<nodes.length;j++){
				var d=nodes[j];
				for(var i=0;i<data01.length;i++){
					if(d.id==data01[i].id){
   						nodes.splice(j,1);
   						--j;
   					}
   				}
			}
		}

		//删除连线
		function deleteLine(){
			var edges_line = d3.selectAll("line")[0];
			for(var j=0;j<edges_line.length;j++){
				var d=edges_line[j];
				for(var i=0;i<data01.length;i++){
					if(d.__data__.source.id==data01[i].id||d.__data__.target.id==data01[i].id){
   						d3.select(d).remove();
   					}
   				}
			}
			//清除被删的连线数据
			for(var j=0;j<edges.length;j++){
				var d=edges[j];
				for(var i=0;i<data01.length;i++){
					if(d.source.id==data01[i].id || d.target.id==data01[i].id ){
						edges.splice(j,1);
   						--j;
   					}
   				}
			}
		}
		//删除节点名称
		function deleteNodeName(){
			var nodes_text = svg.selectAll(".nodetext")[0];
			for(var j=0;j<nodes_text.length;j++){
				var d=nodes_text[j];
				for(var i=0;i<data01.length;i++){
					if(d.hasOwnProperty("__data__")){
						if(d.__data__.id==data01[i].id){
	   						d3.select(d).remove();
	   					}
					}
   				}
			}
		}


		//清空所有
		function clearAll(){
			var elementList = ['image','.nodetext','line','.polyline','.linetext','.zdry']
			$(elementList).each(function(i) {
				clearElement($(elementList)[i]);
			})
			//.pathRoot根元素不清除
		}

		//清除label元素
		function clearElement(label) {
			var elementList=svg.selectAll(label)[0];
			for(var j=0;j<elementList.length;j++){
				d3.select(elementList[j]).remove();
			}
		}

		//删除连线文字
		function deleteLineText(){
			var edges_text = d3.selectAll(".linetext")[0];
			for(var j=0;j<edges_text.length;j++){
				var d=edges_text[j];
				for(var i=0;i<data01.length;i++){
					if(d.__data__.source.id==data01[i].id || d.__data__.target.id==data01[i].id){
   						d3.select(d).remove();
   					}
   				}
			}
		}
		//力学图开始和结束
		function startAndStop(obj){
			if(obj){

				//解锁固定节点
				var image_nodes=d3.selectAll("image")[0];
				for(var j=0;j<image_nodes.length;j++){
					if(image_nodes[j].hasOwnProperty("__data__")){
						image_nodes[j].__data__.fixed = false
					}
				}

				force.start();
			}else{
				force.stop();
			}
		}


		//打开和隐藏筛选框
		function showShaixuan(isShow){
			if(isShow){
				$(".shaixuan_tools_bg").show(1000);
				$(".shaixuan_tools").show(1000);
			}else{
				$(".shaixuan_tools_bg").hide(1000);
				$(".shaixuan_tools").hide(1000);
			}
		}

		//测试工具栏
		function testTool(){
			$(".tools").show(1000);
		}

		//显示和隐藏关系
		function showRelation(isShow){
			var edges_text = d3.selectAll(".linetext")[0];
			if(isShow==1){
				$("#xsgx_font").text("显示关系");
				relation_show=1;
				for(var j=0;j<edges_text.length;j++){
					d3.select(edges_text[j]).style("fill-opacity",1.0);
				}
			}else if(isShow==2){
				$("#xsgx_font").text("隐藏关系");
				relation_show=2;
				for(var j=0;j<edges_text.length;j++){
					d3.select(edges_text[j]).style("fill-opacity",0.0);
				}
			}else if(isShow==3){
				$("#xsgx_font").text("悬浮关系");
				relation_show=3;
				for(var j=0;j<edges_text.length;j++){
					d3.select(edges_text[j]).style("fill-opacity",0.0);
				}
			}
		}
		//节点关系查询类型切换
		function jdgxChange(type){
			if(type==1){
				$("#jdgxcx_font").text("节点");
			}else{
				$("#jdgxcx_font").text("关系");
			}
		}
		//节点关系查询
		function jdgxSearch(){
			if($("#jdgxcx_font").text()=="节点"){
				var nodes_text = svg.selectAll(".nodetext")[0];
				var nodeExists = false;
				var ids = new Array();//nodes中对应的id
				var names = new Array();//选中的text名称
				for(var j=0;j<nodes_text.length;j++){
					var d=nodes_text[j];
					if(d.hasOwnProperty("__data__") && d.__data__.hasOwnProperty("name")){
						if(d.__data__.name.indexOf($("#jdgxcx_text").val())!=-1&&$("#jdgxcx_text").val()!=''){
							d3.select(d).style("display","block");
	   						d3.select(d).classed({'selected':true})
	   						//d3.select(d).style("fill","red")//.style("font-weight","bold").style("font-size","16px");
	   						nodeExists = true;
	   						names.push(d.__data__.name);
	   					}else{
	   						d3.select(d).style("display","none");
	   						d3.select(d).classed({'selected':false})
	   						//d3.select(d).style("fill","#000000")//.style("font-weight","").style("font-size","12px");
	   					}
					}
				}

				//若没有检索到则展示名称
				if(!nodeExists) {
					for(var j=0;j<nodes_text.length;j++){
						var d=nodes_text[j];
						if(d.hasOwnProperty("__data__") && d.__data__.hasOwnProperty("name")){
		   					d3.select(d).style("display","block");
						}
					}
					//清除历史高亮节点
					var paths = d3.selectAll(".pathRoot")[0];
					for(var i = 0; i < paths.length; i++) {
						if(d3.select(paths[i]).attr("lx") != 'initHightLight') {
							d3.select(paths[i]).remove();
						}
					}
				} else {
					for(var k = 0 ; k < names.length; k++) {
						for(var h=0; h < nodes.length; h++) {
							if(nodes[h].name == names[k]) {
								if(ids.indexOf(nodes[h].id) < 0) {
									ids.push(nodes[h].id);
								}
							}
						}
					}
					//alert(ids.join())
					highLightNode(ids.join());
				}
			}else{
				var edges_line = d3.selectAll("line")[0];
				for(var j=0;j<edges_line.length;j++){
					var d=edges_line[j];
					if(d.hasOwnProperty("__data__")  && d.__data__.hasOwnProperty("relation")){
						if(d.__data__.relation.indexOf($("#jdgxcx_text").val())!=-1&&$("#jdgxcx_text").val()!=''){
	   						if(isIE){
	   							d3.select(d).style("marker-end","none");
	   						}else{
	   							d3.select(d).style("marker-end","url(#arrow)");
	   						}
							d3.select(d).style("stroke","red");
	   					}else{
	   						d3.select(d).style("marker-end","url(#arrow)");
	   						d3.select(d).style("stroke","");
	   						//d3.select(d).style("stroke","#ccc");
	   					}
					}
				}

				var edges_text = d3.selectAll(".linetext")[0];
				for(var j=0;j<edges_text.length;j++){
					var d=edges_text[j];
					if(d.hasOwnProperty("__data__") && d.__data__.hasOwnProperty("relation") ){
						if(d.__data__.relation.indexOf($("#jdgxcx_text").val())!=-1&&$("#jdgxcx_text").val()!=''){
	   						d3.select(d).style("fill","red");
	   						d3.select(d).style("fill-opacity",1.0);
	   					}else{
	   						d3.select(d).style("fill","#0000FF");
	   					}
					}
				}
			}
		}

		//显示和隐藏名称
		function showName(isShow){
			var nodes_text = svg.selectAll(".nodetext")[0];
			for(var j=0;j<nodes_text.length;j++){
				var d=nodes_text[j];
				if(isShow){
					$("#xsmc_font").text("显示名称");
					d3.select(d).style("display","block");
				}else{
					$("#xsmc_font").text("隐藏名称");
					d3.select(d).style("display","none");
				}
			}
		}

//根据线的类型展示实体关系（实线）、绘制关系（虚线）
function getLineType(line) {
	var lineClass = "link";
	if(typeof line.type !="undefined" && line.type == 'hz') {
		lineClass = "Hzlink";
	}
	return lineClass;
}

//展示关系连线的权重
function getLineWeight(d) {
	var weight = d.weight;
	if(typeof weight !="undefined" && weight != '') {
		return weight;
	} else {
		return 1;
	}
}

//展示关系连线的颜色
function getLineColor(d) {
    var color = d.color;
    if(typeof color !="undefined" && color != '') {
        return color;
    } else {
        return '#666666';
    }
}

//根据节点类型获取展示相关图片
function getNodeImage(node) {
	var imgUrl = getContextPath();
	if(node.icon == 'Service') {
        imgUrl += "resource/plugins/d3/icon/4.png";
	} else {
        imgUrl += "resource/plugins/d3/icon/1.png";
	}
	setTimeout(function () {
		getNodeIcon(node.id);
	}, 200);
	return imgUrl;
}

/*获取上下文路径*/
function getContextPath() {
	return '';
}


//重置关系图谱，返回当前节点数量
function resetGraph() {
}

function resetGxdata() {
	var index = 0;
	if(nodes != null && nodes.length > 0) {
		for(var j=0;j<nodes.length;j++){
			nodes[j].index = j;
		}
		index = nodes.length;
	}

	if(edges != null && edges.length > 0) {
		for(var j=0;j<edges.length;j++){
			if(nodes != null && nodes.length > 0) {
				for(var i=0;i<nodes.length;i++){
					if(edges[j].fromID == nodes[i].id) {
						edges[j].source = i;
					}
					if(edges[j].toID == nodes[i].id) {
						edges[j].target = i;
					}
				}
			}
		}
	}
	return index;
}

function initGraph(ids, root) {
		//$("#mysvg").parent().append($('<div id="mysvgbak"></div>'))   //为了实现隐藏运动动画
		clearAll();
		//isIE = $.browser.ie;//判断是否是ie
    	isIE = false;//判断是否是ie
		nodeGraph = root;

		for(var i=0;i<root.nodes.length;i++){
			root.nodes[i].index = i;
		}
		for(var i=0;i<root.edges.length;i++){
			for(var j=0;j<root.nodes.length;j++){
				if(root.edges[i].fromID==root.nodes[j].id){
					root.edges[i].source = root.nodes[j].index;
				}
				if(root.edges[i].toID==root.nodes[j].id){
					root.edges[i].target = root.nodes[j].index;
				}
			}
		}

		force = d3.layout.force()
						.nodes(root.nodes)//设定顶点数组
						.links(root.edges)//设定边数组
						.size([width,height])//设定作用范围
						.linkDistance(link_d)//设定边的距离
						.charge(charge)//设定顶点的电荷数
						.start();

		nodes=force.nodes();
		edges=force.links();
		node=vis.selectAll("image");


		edges_line = vis.selectAll(".polyline")
							.data(root.edges)
							.enter()
							.append("path")
							.attr("class",function(d){
								return "polyline " + getLineType(d);
							})
							.attr("id",function(d,i) {
								return d.fromID < d.toID ? (d.fromID+d.toID+i): (d.toID+d.fromID+i);
							})
							.style("stroke",function(d) {
								return getLineColor(d);
							})
							.style("stroke-width",function(d) {
								return getLineWeight(d);
							});

		edges_text = vis.selectAll(".linetext")
							.data(root.edges)
							.enter()
							.append("text")
							.attr("class","linetext")
							.attr("text-anchor","middle")
							.style("fill-opacity",relation_show==1?1.0:0.0)//新加判断
							.text(function(d){
								//return d.relation;
							})
							.on("mousedown",function(d,i){
								toDetail(d);
								event.stopPropagation();
							});

		edges_text
							.append("textPath")
							.attr("xlink:href",function(d,i) {
								return "#"+ (d.fromID < d.toID ? (d.fromID+d.toID+i): (d.toID+d.fromID+i));
							})
							.attr("startOffset","50%")
							.text(function(d){
								return d.relation;
							});


		drag = force.drag()
					.on("dragstart",function(d,i){
						d.fixed = true;    //拖拽开始后设定被拖拽对象为固定
						node_start_x=d.x;
						node_start_y=d.y;
					})
					.on("dragend",function(d,i){
						//label_text_2.text("拖拽状态：结束");
						//alert(d.x+","+d.y+":"+d.px+","+d.py);
						//alert((d.x-node_start_x)+","+(d.y-node_start_y));
					})
					.on("drag",function(d,i){
						//label_text_2.text("拖拽状态：进行中");
					});

		if(ids != null && ids != '') {
			var idArray = ids.split(',');
			for(var m = 0; m < idArray.length; m++) {
				vis.append("path")
				.attr("class","pathRoot")
				.attr("fill","none")
				.attr("stroke","#EE6911")
				.attr("stroke-width","1")
				.attr("lx","initHightLight")
				.attr("id","bs_"+idArray[m]);
			}
		}

		root.nodes.forEach(function(d,i){
			if(d.sfzdry != '' && d.sfzdry != "undefined" && typeof d.sfzdry !="undefined") {
				vis.append("path")
				.attr("stroke","none")
				.attr("fill","red")
				.attr("class","zdry")
				.attr("id","zdry_"+d.id);
			}
		});


		nodes_img = vis.selectAll("image")
							.data(root.nodes)
							.enter()
							.append("image")
							.attr("id",function(d){
								d.fixed = false;
								return d.id;
							})
							.attr("width",img_w)
							.attr("height",img_h)
							.attr("xlink:href",function(d){
								return getNodeImage(d);
							})
							.on("mouseover",function(d,i){
								isOverNode=true;
								//显示连接线上的文字
								if(relation_show==3){//新加判断
									edges_text.style("fill-opacity",function(edge){
										if( edge.source === d || edge.target === d ){
											return 1.0;
										}
									});
								}
							})
							.on("mouseout",function(d,i){
								isOverNode=false;
								//隐去连接线上的文字
								if(relation_show==3){//新加判断
									edges_text.style("fill-opacity",function(edge){
										if( edge.source === d || edge.target === d ){
											return 0.0;
										}
									});
								}
							})
							.on("mousedown",function(d,i){
								isChoiceNode=true;
								choiceNode=this;
								choiceNode2=d;
								choiceNodeIndex=i;
							})
							.on("dblclick",function(d,i){
								d.fixed = false;
							})
							.on("click",function(d,i){
								getJbxx(d);
							})
							.call(drag);

		var text_dx = -20;
		var text_dy = 20;

		nodes_text = vis.selectAll(".nodetext")
							.data(root.nodes)
							.enter()
							.append("text")
							.attr("class","nodetext")
							.attr("dx",text_dx)
							.attr("dy",text_dy)
							.text(function(d){
								return d.name;
							})
							;





		setLineSize(edges_line);

		//力学图运动结束时
		force.on("end", function(){
			//label_text_1.text("运动状态：结束");
		});

		force.on("tick", function(){

			//修改标签文字
			//限制结点的边界
			root.nodes.forEach(function(d,i){
				d.x = d.x - img_w/2 < 0     ? img_w/2 : d.x ;
				d.x = d.x + img_w/2 > width ? width - img_w/2 : d.x ;

				d.y = d.y - img_h/2 < 0      ? img_h/2 : d.y ;
				d.y = d.y + img_h/2 + text_dy > height ? height - img_h/2 - text_dy : d.y ;

			});

			//更新连接线的位置
			/* edges_line.attr("x1",function(d){
				 return d.source.x; }
			 );
			 edges_line.attr("y1",function(d){
				 return d.source.y; }
			 );*/

			 edges_line.each(function(d) {
		            if (isIE) {
		                this.parentNode.insertBefore(this, this);
		            }





				d3.select(this).attr('d', lineMove(d));
			 });

			 //更新连接线上文字的位置
			edges_text.attr("x",function(d){
				 //return d.ex;
			});

			edges_text.attr("y",function(d){
				 //return  d.ey;
			});

			 /*
			edges_text.attr("transform",function(d){
				return d.fromID < d.toID ? "": "rotate(180 " + d.ex + "," + d.ey+")";
			})*/

			edges_text.attr("transform",function(d){
				if(d.target.x > d.source.x ) {
					//目标点在右侧
					return "";
				} else {
					//目标点在左侧
					return "rotate(180 " + d.ex + "," + d.ey+")";

				}
			})

			 //更新结点图片和文字
			 nodes_img.attr("x",function(d){ return d.x - img_w/2; });
			 nodes_img.attr("y",function(d){ return d.y - img_h/2; });

			 nodes_text.attr("x",function(d){ return d.x });
			 nodes_text.attr("y",function(d){ return d.y + img_w/2; });

			 root.nodes.forEach(function(d,i){
				 var a = d.x-img_w/2;
				 var b = d.y-img_h/2;
				 var a1 = a;
				 var b1 = b;
				 var a2 = a+img_w;
				 var b2 = b;
				 var a3 = a+img_w;
				 var b3 = b+img_h;
				 var a4 = a;
				 var b4 = b+img_h;
				 if(d.sfzdry != '' && d.sfzdry != "undefined" && typeof d.sfzdry !="undefined") {
					 var starPos = "M"
					 for(var i = 0; i < 10; i+=2) {
						 var x = Math.sin(Math.PI*2/5*i)*15+8+a2;
						 var y = Math.cos(Math.PI*2/5*i)*15+b2;
						 if(i==0) {
							 starPos = starPos + x+ "," + y;
						 } else {
							 starPos = starPos + " L" + x+"," + y;
						 }
					 }
					 starPos += " z";
					 d3.select("#zdry_"+d.id).attr("d",starPos);
				 }

				 if(ids.indexOf(d.id) > -1) {

					 var position = "M" + a1 + " " + b1 + " "
						 + "A 20 20 0 1 0 " +  a3 + " " + b3
						 + "A 20 20 0 1 0 " +  a1 + " " + b1;//（根节点画圆，小于实际半径即可）
					 vis.select("#bs_"+d.id).attr("d",position)
				 }
			 });
		});
		center_view();//居中
}


//运动之前根据连线的数量设置属性
function setLineSize(edges_line) {
	var lMap={};

	//运动之前计算两点间连线的数量、角度分配
	edges_line.each(function(d) {
		var f = d.fromID;
		var t = d.toID;
		var l = f < t ? (f+"_"+t) : (t + "_" +f);
		if(typeof lMap[l] == "undefined") {
			var lArray = new Array();
			lMap[l] = 1;
			d.rn = 1;
		} else {
			lMap[l] = lMap[l] +1;
			d.rn = lMap[l];
		}
	});

	edges_line.each(function(d) {
		var f = d.fromID;
		var t = d.toID;
		var l = f < t ? (f+"_"+t) : (t + "_" +f);
		d.rnMax = lMap[l];

		if(d.rnMax == 1) {
			d.k = 0;
			d.sx = '1';
		} else {
			if(d.rnMax%2 == 0) {
				//偶数条线
				if(d.rn <= d.rnMax/2) {
					d.k = d.rn/(d.rnMax/2);//斜率
					d.sx = '1';//向上
				} else {
					d.k = (d.rn-d.rnMax/2)/(d.rnMax/2);
					d.sx = '2';//向下
				}
			} else {
				if(d.rn == (d.rnMax+1)/2) {
					d.k = 0;
					d.sx = '1';
				} else if(d.rn < d.rnMax/2) {
					d.k = d.rn/parseInt(d.rnMax/2);
					d.sx = '1';
				} else {
					d.k = (d.rn-(d.rnMax+1)/2)/parseInt(d.rnMax/2);
					d.sx = '2';
				}
			}
		}
	});
}

function lineMove(d) {
	 var x1 = d.source.x,
		y1 = d.source.y;
		x2 = d.target.x,
		y2 = d.target.y;

	var f = d.fromID;
	var t = d.toID;
	var qd = "1";//从小到大
	if(f < t) {
	} else {
		x1 = d.target.x,
		y1 = d.target.y;
		x2 = d.source.x,
		y2 = d.source.y;
		qd = "2";//从大到小
	}

	/*
	已知线段两端坐标,求线段上距离一端距离为L的点的坐标
	已知一条线段两端坐标x1,y1 ---- x2,y2
	现在求此线段上的一点的坐标,要求此点距离x1,y1,距离为L
	*/
	var a = pointXY(x1,y1,x2,y2)
	var b = pointXY(x2,y2,x1,y1)
	var p1x=Number(a.split(',')[0])
	var p1y=Number(a.split(',')[1])

	var p2x =Number(b.split(',')[0])
	var p2y =Number(b.split(',')[1])

	var points_zh = getPoints(d,d.k,d.sx,p1x,p2x,p1y,p2y,qd);
	//var points = getPoints(d,d.k,d.sx,x1,x2,y1,y2,qd);

	//var p1 = points.split(" ")[1];
	//var p2 = points.split(" ")[2];

	//d.ex = (Number(p1.split(",")[0].replace(/M|L|C|A/g,''))+Number(p2.split(",")[0].replace(/M|L|C|A/g,'')))/2;
	//d.ey = (Number(p1.split(",")[1].replace(/M|L|C|A/g,''))+Number(p2.split(",")[1].replace(/M|L|C|A/g,'')))/2;
	var zjds = points_zh.split('||')[1];
	var p1 = zjds.split(" ")[0];
	var p2 = zjds.split(" ")[1];
	d.ex = (Number(p1.split(",")[0].replace(/M|L|C|A/g,''))+Number(p2.split(",")[0].replace(/M|L|C|A/g,'')))/2;
	d.ey = (Number(p1.split(",")[1].replace(/M|L|C|A/g,''))+Number(p2.split(",")[1].replace(/M|L|C|A/g,'')))/2;

	return points_zh.split('||')[0];
}

//求线段内距离端点（p1x,p1y）的点的坐标（内部）
function pointXY(p1x,p1y,p2x,p2y) {
        var  distance = Math.sqrt(Math.pow(p1x - p2x, 2)
                + Math.pow(p1y - p2y, 2));// 两点的坐标距离
        var  lenthUnit = img_w / 2;// 单位长度
        // 第一步：求得直线方程相关参数y=kx+b
        var  k = (p1y - p2y) * 1.0
                / (p1x - p2x);// 坐标直线斜率k
        var  b = p1y - k * p1x;// 坐标直线b
        // 第二步：求得在直线y=kx+b上，距离当前坐标距离为L的某点
        // 一元二次方程Ax^2+Bx+C=0中,
        // 一元二次方程求根公式：
        // 两根x1,x2= [-B±√(B^2-4AC)]/2A
        // ①(y-y0)^2+(x-x0)^2=L^2;
        // ②y=kx+b;
        // 式中x,y即为根据以上lenthUnit单位长度(这里就是距离L)对应点的坐标
        // 由①②表达式得到:(k^2+1)x^2+2[(b-y0)k-x0]x+[(b-y0)^2+x0^2-L^2]=0
        var  A = Math.pow(k, 2) + 1;// A=k^2+1;
        var  B = 2 * ((b - p1y) * k - p1x);// B=2[(b-y0)k-x0];
        var  m = 1;
        var  L = m * lenthUnit;
        // C=(b-y0)^2+x0^2-L^2
        var  C = Math.pow(b - p1y, 2) + Math.pow(p1x, 2)
                - Math.pow(L, 2);
        // 两根x1,x2= [-B±√(B^2-4AC)]/2A
        var  x1 = (-B + Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
        var  x2 = (-B - Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
        var  x = 0;// 最后确定是在已知两点之间的某点
        if (x1 == x2) {
            x = x1;
        } else if (p1x <= x1 && x1 <= p2x || p2x <= x1
                && x1 <= p1x) {
            x = x1;
        } else if (p1x <= x2 && x2 <= p2x || p2x <= x2
                && x2 <= p1x) {
            x = x2;
        }
        var  y = k * x + b;
        return x+','+y;
}

//k 分配的角度系数 sx表示向上还是向下分配（废弃）
function getPoints2(d,k,sx,x1,x2,y1,y2, qd) {
	//alert(d)
	var a  =  40;
	var jd= Math.PI/6*k;

	if(sx == '1') {
		var m = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
		var xc = x1 + a*(x2-x1)/m - a*Math.tan(jd)*(y2-y1)/m;
		var yc = y1 + a*(y2-y1)/m+a*Math.tan(jd)*(x2-x1)/m;

		var m = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		var xd = x2 + a*(x1-x2)/m - a*Math.tan(-jd)*(y1-y2)/m;
		var yd = y2 + a*(y1-y2)/m+a*Math.tan(-jd)*(x1-x2)/m;
		//return 'M'+x1+","+y1 + " " + "L"+(xc) + "," + (yc) + " " + "L"+(xd) + "," + (yd) 				+ " "+ "L"+x2 + "," + y2 ;//2017-12-06 从小到大
		if(qd == '1') {
			return 'M'+x1+","+y1 + " " + "L"+(xc) + "," + (yc) + " " + "L"+(xd) + "," + (yd)
				+ " "+ "L"+x2 + "," + y2 ;
		} else {
			return 'M'+x2+","+y2 + " " + "L"+(xd) + "," + (yd) + " " + "L"+(xc) + "," + (yc)
				+ " "+"L"+ x1 + "," + y1 ;
		}
	} else {
		var m = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		var xe = x2 + a*(x1-x2)/m - a*Math.tan(jd)*(y1-y2)/m;
		var ye = y2 + a*(y1-y2)/m+a*Math.tan(jd)*(x1-x2)/m;

		var m = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
		var xf = x1 + a*(x2-x1)/m - a*Math.tan(-jd)*(y2-y1)/m;
		var yf = y1 + a*(y2-y1)/m+a*Math.tan(-jd)*(x2-x1)/m;
		//return 'M'+x1+","+y1 + " " + "L"+(xf) + "," + (yf) + " " + "L"+(xe) + "," + (ye) 				+ " "+ "L"+ x2 + "," + y2 ;//2017-12-06 从小到大
		if(qd == '1') {
			return 'M'+x1+","+y1 + " " + "L"+(xf) + "," + (yf) + " " + "L"+(xe) + "," + (ye)
				+ " "+ "L"+ x2 + "," + y2 ;
		} else {
			return 'M'+x2+","+y2 + " " + "L"+(xe) + "," + (ye) + " " +"L"+ (xf) + "," + (yf)
				+ " "+ "L"+x1 + "," + y1 ;
		}
	}
}

//k 分配的角度系数 sx表示向上还是向下分配
function getPoints(d,k,sx,x1,x2,y1,y2, qd) {
	var a  =  40;
	var jd= Math.PI/6*k;
	if(k == 0) {
		if(qd == '1') {
			return 'M'+x1+","+y1 + " L"+x2 + "," + y2 +"||" + x1+","+y1 + ' ' + x2 + "," + y2;
		} else {
			return 'M'+x2+","+y2 + " L"+x1 + "," + y1 +"||" + x2+","+y2 + ' ' + x1 + "," + y1;
		}

	} else {
		if(sx == '1') {
			var m = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			var xc = x1 + a*(x2-x1)/m - a*Math.tan(jd)*(y2-y1)/m;
			var yc = y1 + a*(y2-y1)/m+a*Math.tan(jd)*(x2-x1)/m;

			var m = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
			var xd = x2 + a*(x1-x2)/m - a*Math.tan(-jd)*(y1-y2)/m;
			var yd = y2 + a*(y1-y2)/m+a*Math.tan(-jd)*(x1-x2)/m;

			if(qd == '1') {
				return 'M'+x1+","+y1 + " " + "A45,45 0 0 0,"+(xc) + "," + (yc) + " L" + ""+(xd) + "," + (yd)
					+ " A45,45 0 0 0,"+ ""+x2 + "," + y2 + "||" + xc+","+yc + ' ' + xd + "," + yd;
			} else {
				return 'M'+x2+","+y2 + " " + "A45,45 0 0 1,"+(xd) + "," + (yd) + " L" + ""+(xc) + "," + (yc)
					+ " A45,45 0 0 1,"+""+ x1 + "," + y1 + "||" + xd+","+yd + ' ' + xc + "," + yc;
			}
		} else {
			var m = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
			var xe = x2 + a*(x1-x2)/m - a*Math.tan(jd)*(y1-y2)/m;
			var ye = y2 + a*(y1-y2)/m+a*Math.tan(jd)*(x1-x2)/m;

			var m = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			var xf = x1 + a*(x2-x1)/m - a*Math.tan(-jd)*(y2-y1)/m;
			var yf = y1 + a*(y2-y1)/m+a*Math.tan(-jd)*(x2-x1)/m;

			if(qd == '1') {
				return 'M'+x1+","+y1 + " " + "A45,45 0 0 1,"+(xf) + "," + (yf) + " L" + ""+(xe) + "," + (ye)
					+ " A45,45 0 0 1,"+ ""+ x2 + "," + y2 + "||" + xf+","+yf + ' ' + xe + "," + ye;
			} else {
				return 'M'+x2+","+y2 + " " + "A45,45 0 0 0,"+(xe) + "," + (ye) + " L" +""+ (xf) + "," + (yf)
					+ " A45,45 0 0 0,"+ ""+x1 + "," + y1 + "||" + xe+","+ye + ' ' + xf + "," + yf;
			}
		}
	}
}

//var loadIndex;
function expandGraph(root){
		/*loadIndex = layer.load()
		var obj = $("#mysvg").html();
		$("#mysvgbak").append($(obj));*/
		clearAll();
		/*$("#mysvg").css("display","none");
		$("#mysvgbak").find("#arrow").attr("id","arrowbak");
		$(".link").css("marker-end","url(#arrowbak)");
		$("#mysvgbak").css("display","block");*/

		var r= 1;//半径
		var zc = 2*Math.PI*r;//周长
		var hc = zc/root.nodes.length; //弧长
		var yxj = hc/r;//圆心角
		var x1 = 0;
		var y1 = 0;

		var maxIndex = resetGxdata();
		for(var i=0;i<root.nodes.length;i++){
			var nodeExists = false;
			if(choiceNode2 != null) {
				if(i == 0) {
					x1 = choiceNode2.x+1;
					y1 = choiceNode2.y;
				} else {
					var jj = yxj-Math.asin((x1-choiceNode2.x)/r);
					x1 = Math.sin(jj)+choiceNode2.x;
					y1 = Math.cos(jj)+choiceNode2.y;
				}
				root.nodes[i].x = x1;
				root.nodes[i].y = y1;
				root.nodes[i].px = x1;
				root.nodes[i].py = y1;
			}

			if(nodes != null) {
				for(var j=0;j<nodes.length;j++){
					if(root.nodes[i].id==nodes[j].id){
						nodeExists = true;
						break;
					}
				}
			} else {
				nodes = new Array();
			}

			if(!nodeExists) {
				root.nodes[i].index = maxIndex;
				nodes.push(root.nodes[i]);
				maxIndex++;
			}
		}


		for(var i=0;i<root.edges.length;i++){
			if(edges != null) {
				for(var j=0;j<nodes.length;j++){
					if(root.edges[i].fromID==nodes[j].id){
						root.edges[i].source = nodes[j].index;
					}
					if(root.edges[i].toID==nodes[j].id){
						root.edges[i].target = nodes[j].index;
					}
				}
			} else {
				edges = new Array();
			}
			var edgeExists = false;
			if(edges != null) {
				for(var j=0;j<edges.length;j++){
					if((root.edges[i].toID==edges[j].toID&&root.edges[i].fromID==edges[j].fromID) ||
							(root.edges[i].fromID==edges[j].toID&&root.edges[i].toID==edges[j].fromID)){
						if(typeof edges[j].relation !="undefined" && edges[j].relation.indexOf(root.edges[i].relation) < 0) {
							edges[j].relation+=","+root.edges[i].relation;
							root.edges[i].relation="";
						}
					}

					if(root.edges[i].toID==edges[j].toID&&root.edges[i].fromID==edges[j].fromID){
						edgeExists = true;
					}
				}
			}

			if(!edgeExists) {
				edges.push(root.edges[i]);
			}
		}

		edges_line = vis.selectAll(".polyline")
							.data(edges)
							.enter()
							.append("path")
							.attr("class",function(d){
								return "polyline " + getLineType(d);
							})
							.attr("id",function(d,i) {
								return d.fromID < d.toID ? (d.fromID+d.toID+i): (d.toID+d.fromID+i);
							})
							.style("stroke",function(d) {
								return getLineColor(d);
							})
							.style("stroke-width",function(d) {
								return getLineWeight(d);
							});;

		edges_text = vis.selectAll(".linetext")
							.data(edges)
							.enter()
							.append("text")
							.attr("class","linetext")
							.attr("text-anchor","middle")
							.style("fill-opacity",relation_show==1?1.0:0.0)//新加判断
							.text(function(d){
								//return d.relation;
							}).on("mousedown",function(d,i){
								toDetail(d);
								event.stopPropagation();
							});

		edges_text
					.append("textPath")
					.attr("xlink:href",function(d,i) {
						return "#"+ (d.fromID < d.toID ? (d.fromID+d.toID+i): (d.toID+d.fromID+i));
					})
					.attr("transform",function(d) {
						return (d.fromID < d.toID ? "": "rotate(180)");
					})
					.attr("startOffset","50%")
					.text(function(d){
						return d.relation;
					});

		nodes_img = vis.selectAll("image")
							.data(nodes)
							.enter()
							.append("image")
							.attr("class","imagenode")
							.attr("id",function(d){
								return d.id;
							})
							.attr("width",img_w)
							.attr("height",img_h)
							.attr("xlink:href",function(d){
								//return d.image;
								return getNodeImage(d);
							})
							.on("mouseover",function(d,i){
							isOverNode=true;
							//显示连接线上的文字
							if(relation_show==3){//新加判断
									edges_text.style("fill-opacity",function(edge){
										if( edge.source === d || edge.target === d ){
											return 1.0;
										}
									});
								}
							})
							.on("mouseout",function(d,i){
								isOverNode=false;
								//隐去连接线上的文字
								if(relation_show==3){//新加判断
									edges_text.style("fill-opacity",function(edge){
										if( edge.source === d || edge.target === d ){
											return 0.0;
										}
									});
								}
							})
							.on("mousedown",function(d,i){
								isChoiceNode=true;
								choiceNode=this;
								choiceNode2=d;
								choiceNodeIndex=i;
							})
							.on("dblclick",function(d,i){
								d.fixed = false;
							})
							.on("click",function(d,i){
								getJbxx(d);
							})
							.call(drag);


		var text_dx = -20;
		var text_dy = 20;

		nodes_text = vis.selectAll(".nodetext")
							.data(nodes)
							.enter()
							.append("text")
							.attr("class","nodetext")
							.attr("dx",text_dx)
							.attr("dy",text_dy)
							.text(function(d){
								return d.name;
							});

		nodes.forEach(function(d,i){
			if(d.sfzdry != '' && d.sfzdry != "undefined" && typeof d.sfzdry !="undefined") {
				vis.append("path")
				.attr("stroke","none")
				.attr("fill","red")
				.attr("class","zdry")
				.attr("id","zdry_"+d.id);
			}
		});

		force.friction(0.5);

		//力学图运动开始时
		force.on("start", function(){
			//label_text_1.text("运动状态：开始");
		});

		//力学图运动结束时
		force.on("end", function(){
			//label_text_1.text("运动状态：结束");
			/*layer.close(loadIndex)
			$("#mysvg").css("display","block");
			$("#mysvgbak").html('');
			$("#mysvgbak").css("display","none");*/
		});

		setLineSize(edges_line);
		force.on("tick", function(){

			//限制结点的边界
			nodes.forEach(function(d,i){
				d.x = d.x - img_w/2 < 0     ? img_w/2 : d.x ;
				d.x = d.x + img_w/2 > width ? width - img_w/2 : d.x ;
				d.y = d.y - img_h/2 < 0      ? img_h/2 : d.y ;
				d.y = d.y + img_h/2 + text_dy > height ? height - img_h/2 - text_dy : d.y ;
			});

			//更新连接线的位置
			// edges_line.attr("x1",function(d){ return d.source.x; });
			// edges_line.attr("y1",function(d){ return d.source.y; });
			 //edges_line.attr("x2",function(d){ return d.target.x; });
			 //edges_line.attr("y2",function(d){ return d.target.y; });

			 edges_line.each(function(d) {
		            if (isIE) {
		                // Work around IE bug regarding paths with markers
		                // Credit: #6 and http://stackoverflow.com/a/18475039/106302
		            	if(this.parentNode!=null){
		            		this.parentNode.insertBefore(this, this);
		            	}
		            }

					d3.select(this).attr('d', lineMove(d));
			 });

			 edges_text.attr("transform",function(d){
				if(d.target.x > d.source.x ) {
					//目标点在右侧
					return "";
				} else {
					//目标点在左侧
					return "rotate(180 " + d.ex + "," + d.ey+")";

				}
			})

			 //更新结点图片和文字
			 //sss.attr("x",function(d){ return d.x - img_w/2; });
			 //sss.attr("y",function(d){ return d.y - img_h/2; });
			 nodes_img.attr("x",function(d){ return d.x - img_w/2; });
			 nodes_img.attr("y",function(d){ return d.y - img_h/2; });


			 nodes_text.attr("x",function(d){ return d.x });
			 nodes_text.attr("y",function(d){ return d.y + img_w/2; });

			 nodes.forEach(function(d,i){
				 var a = d.x-img_w/2;
				 var b = d.y-img_h/2;
				 var a1 = a;
				 var b1 = b;
				 var a2 = a+img_w;
				 var b2 = b;
				 var a3 = a+img_w;
				 var b3 = b+img_h;
				 var a4 = a;
				 var b4 = b+img_h;

				 if(d.sfzdry != '' && d.sfzdry != "undefined" && typeof d.sfzdry !="undefined") {
					 var starPos = "M"
					 for(var i = 0; i < 10; i+=2) {
						 var x = Math.sin(Math.PI*2/5*i)*15+8+a2;
						 var y = Math.cos(Math.PI*2/5*i)*15+b2;
						 if(i==0) {
							 starPos = starPos + x+ "," + y;
						 } else {
							 starPos = starPos + " L" + x+"," + y;
						 }
					 }
					 starPos += " z";
					 d3.select("#zdry_"+d.id).attr("d",starPos);
				 }

				 var paths = d3.selectAll(".pathRoot")[0];
				 for(var i = 0; i < paths.length; i++) {
					 if(d.id == paths[i].id.substr(3)) {
						 //rect.attr("x",d.x-img_w/2);
						 //rect.attr("y",d.y-img_h/2);

						 var position = "M" + a1 + " " + b1 + " "
					 		+ "L" + a2 + " " + b2 + " "
					 		+ "L" + a3 + " " + b3 + " "
					 		+ "L" + a4 + " " + b4 + " Z";
						 if(d3.select(paths[i]).attr("lx") == 'initHightLight') {
							 position = "M" + a1 + " " + b1 + " "
							 + "A 20 20 0 1 0 " +  a3 + " " + b3
							 + "A 20 20 0 1 0 " +  a1 + " " + b1;//（根节点画圆，小于实际半径即可）
						 }
						 d3.select(paths[i]).attr("d",position);
						 break;
					 }
				 }
			 })

		});

		//alert("拓展成功2！");
		force.stop();//停止运动
		force.start();//开始运动
		delNum=0;//删除数量归零
//startAndStop(true)
}

//进入一查通页面
function toYct(lx, id) {

}

//高亮节点照片
function highLightNode(ids) {
	if(ids != null && ids != '') {
		//清除历史高亮节点
		var paths = d3.selectAll(".pathRoot")[0];
		for(var i = 0; i < paths.length; i++) {
			if(d3.select(paths[i]).attr("lx") != 'initHightLight') {
				d3.select(paths[i]).remove();
			}
		}

		var idArray = ids.split(',');
		for(var m = 0; m < idArray.length; m++) {
			vis.append("path")
			.attr("class","pathRoot")
			.attr("fill","none")
			.attr("stroke","red")
			.attr("stroke-width","1")
			.attr("id","bs_"+idArray[m]);
		}
		var text_dx = -20;
		var text_dy = 20;

		force.on("tick", function(){

			//限制结点的边界
			nodes.forEach(function(d,i){
				d.x = d.x - img_w/2 < 0     ? img_w/2 : d.x ;
				d.x = d.x + img_w/2 > width ? width - img_w/2 : d.x ;
				d.y = d.y - img_h/2 < 0      ? img_h/2 : d.y ;
				d.y = d.y + img_h/2 + text_dy > height ? height - img_h/2 - text_dy : d.y ;
			});

			//更新连接线的位置
			 edges_line.attr("x1",function(d){ return d.source.x; });
			 edges_line.attr("y1",function(d){ return d.source.y; });

			 edges_line.each(function(d) {
		            if (isIE) {
		            	if(this.parentNode!=null){
		            		this.parentNode.insertBefore(this, this);
		            	}
		            }

		            var x1 = d.source.x,
	                    y1 = d.source.y;
		            	x2 = d.target.x,
		            	y2 = d.target.y;

		            var l=Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
		            var l1=20;//箭头方向缩短的长度
		            var x3=x1+((l-l1)/l)*(x2-x1);
		            var y3=y1+((l-l1)/l)*(y2-y1);

		            d3.select(this)
		                .attr('x2', x3)
		                .attr('y2', y3);
			 });

			 //更新连接线上文字的位置
			 //edges_text.attr("x",function(d){ return (d.source.x + d.target.x) / 2 ; });
			 //edges_text.attr("y",function(d){ return (d.source.y + d.target.y) / 2 ; });


			 //更新结点图片和文字
			 nodes_img.attr("x",function(d){ return d.x - img_w/2; });
			 nodes_img.attr("y",function(d){ return d.y - img_h/2; });


			 nodes_text.attr("x",function(d){ return d.x });
			 nodes_text.attr("y",function(d){ return d.y + img_w/2; });

			 nodes.forEach(function(d,i){
				 var a = d.x-img_w/2;
				 var b = d.y-img_h/2;
				 var a1 = a;
				 var b1 = b;
				 var a2 = a+img_w;
				 var b2 = b;
				 var a3 = a+img_w;
				 var b3 = b+img_h;
				 var a4 = a;
				 var b4 = b+img_h;

				 if(d.sfzdry != '' && d.sfzdry != "undefined" && typeof d.sfzdry !="undefined") {
					 var starPos = "M"
					 for(var i = 0; i < 10; i+=2) {
						 var x = Math.sin(Math.PI*2/5*i)*15+8+a2;
						 var y = Math.cos(Math.PI*2/5*i)*15+b2;
						 if(i==0) {
							 starPos = starPos + x+ "," + y;
						 } else {
							 starPos = starPos + " L" + x+"," + y;
						 }
					 }
					 starPos += " z";
					 d3.select("#zdry_"+d.id).attr("d",starPos);
				 }

				 var paths = d3.selectAll(".pathRoot")[0];
				 for(var i = 0; i < paths.length; i++) {
					 if(d.id == paths[i].id.substr(3)) {
						 var position = "M" + a1 + " " + b1 + " "
					 		+ "L" + a2 + " " + b2 + " "
					 		+ "L" + a3 + " " + b3 + " "
					 		+ "L" + a4 + " " + b4 + " Z";
						 if(d3.select(paths[i]).attr("lx") == 'initHightLight') {
							 position = "M" + a1 + " " + b1 + " "
							 + "A 20 20 0 1 0 " +  a3 + " " + b3
							 + "A 20 20 0 1 0 " +  a1 + " " + b1;//（根节点画圆，小于实际半径即可）
						 }
						 d3.select(paths[i]).attr("d",position);
						 break;
					 }
				 }
			 })

		});

		force.stop();//停止运动
		force.start();//开始运动
	}
}

function closeLayer() {
	layer.close(hoverIndex)
}