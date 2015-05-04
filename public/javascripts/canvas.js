var cy = cytoscape({
	container: document.getElementById('canvas'),
	
	style: cytoscape.stylesheet()
		.selector('node')
			.css({
				'height': 80,
				'width': 80,
				'background-fit': 'cover',
				'background-color': '#3C80F6',
				'content': 'data(label)',
				'color': 'yellow',
				'text-valign': 'center'
			})
		.selector('edge')
			.css({
				'width': 6,
				'target-arrow-shape': 'triangle',
				'line-color': '#5cb85c',
				'target-arrow-color': '#5cb85c'
			})
}); 

cy.on('click', 'node', function(event){
	var node = $(this)[0];
	var modal = $('#CanvasEditModal');
	
	var id = node.id();
	var name = node.data('label');
	var arguments = node.data('arguments');
	var form = $('<div><form id="CanvasEditModal_form"><input id="CanvasEditModal_input" type="text" value="'+arguments+'" required></form></div>');
	form.find("#CanvasEditModal_form").submit(function(event){
		event.preventDefault();
		cy.getElementById(id).data( 'arguments', modal.find('input').val() );
		modal.modal('hide');
	});
	
	var txt = 'Your command line arguments are:<br>';
	var del = $('<button type="button" class="btn btn-danger">Delete</button>');
	del.on("click", function(){onNodeDelete(id, modal);});
	
	modal.find('.modal-title').empty().append("Edit Node "+name);
	modal.find('.modal-body').empty().append(txt, form);
	modal.find('.modal-footer').empty().append(
		'<button type="submit" form="CanvasEditModal_form" class="btn btn-success">Save</button>'
		,del,
		'<button type="button" class="btn btn-primary" data-dismiss="modal">Abort</button>');
	
	modal.modal('show');
});

function onNodeDelete(id, modal){
	var edges = cy.$("#"+id).connectedEdges();
	if(edges.size() > 1){
		var source = edges.first().source();
		var target = edges.last().target();
		cy.add([
			{ group: "edges", data: { source: source.id(), target: target.id() } }
		]);
	}
	cy.remove(cy.$("#"+id));
	modal.modal('hide');
}

$(".btn-add").click( function(event) {
	var elementLabel = $(event.target).closest(".program-element").find(".prog-name").text();
	var elementId = "" + new Date().getTime();
	var oldElements = cy.elements('*').jsons();
	var newElement = [];
	var lastElement = cy.nodes("*").leaves().first();
	
	if(oldElements.length == 0)
		newElement = [	{ group: "nodes", data: { id: elementId, label: elementLabel, arguments: ""}}];
	else{
		newElement = [	{ group: "nodes", data: { id: elementId, label: elementLabel, arguments: ""}},
						{ group: "edges", data: { source: lastElement.id(), target: elementId}} ];
	}
	
	// Can't use cy.add, because a position is needed, which is not available. Workaround: cy.load
	cy.load(oldElements.concat(newElement));
});

$('#downloadProject').click( function(){
	var data = cy.elements('*').jsons();
	var pom = document.createElement('a');
	
	pom.setAttribute('href', 'data:application/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(data)));
	pom.setAttribute('download', 'script');
	pom.style.display = 'none';
	document.body.appendChild(pom);
	
	pom.click();
	
	document.body.removeChild(pom);
});

$('#loadProject').click( function(){
	//TODO Open File Chooser, select file, read file in JS without uploading, load with cytoscape
	var elements = {};
	cy.load(elements);
});

$('#runProject').click( function(){
	//Extract sequence
	var nodes = cy.nodes("*");
	var root = cy.nodes().roots().first();
	var edges = root.edgesTo(nodes);
	
	var seq = [{name: root.data("label"), args: root.data("arguments")}];
	
	while(edges.size() >= 1){
		var next = edges.first().target();
		edges = next.edgesTo(nodes);
		seq.push({name: next.data("label"), args: next.data("arguments")});
	}
	console.log(seq);
	
	var script = "#!/bin/bash\n\n";
	for(i in seq) {
		script += seq[i].name + " " + seq[i].args;
		if(i != seq.length-1) script += "|\n";
	}

	var pom = document.createElement('a');
	
	pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(script));
	pom.setAttribute('download', 'script.sh');
	pom.style.display = 'none';
	document.body.appendChild(pom);
	
	pom.click();
	
	document.body.removeChild(pom);
});