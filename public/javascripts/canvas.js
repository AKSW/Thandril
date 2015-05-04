/** 
 * Cytoscape Config
 */

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

/** 
 * Open up a new modal, containing some vital Informations about the program
 */
cy.on('click', 'node', function(event){
	var node = $(this)[0];
	var modal = $('#CanvasEditModal');
	
	var id = node.id();
	var name = node.data('label');
	var arguments = node.data('arguments');
	var form = $('<div><form id="CanvasEditModal_form"><input id="CanvasEditModal_input" type="text" value="'+arguments+'" required></form></div>');
	
	form.find("#CanvasEditModal_form").submit(function(event){ event.preventDefault(); onNodeSave(modal); });
	
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

/** 
 * Save changes to the arguments of a program
 */
function onNodeSave(modal){
	
	cy.getElementById(id).data( 'arguments', modal.find('input').val() );
	modal.modal('hide');
}

/** 
 * Delete a program from the canvas and fix sequence
 */
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

/** 
 * Add a new program to the canvas, specified by an user action
 */
$(".btn-add").click( function(event) {
	var elementLabel = $(event.target).closest(".program-element").find(".prog-name").text();
	var elementId = "" + new Date().getTime();
	var oldElements = cy.elements('*').jsons();
	var newElement = [];
	var lastElement = cy.nodes("*").leaves().first();
	var edgeID= elementId + "1";
	
	if(oldElements.length == 0)
		newElement = [	{ group: "nodes", data: { id: elementId, label: elementLabel, arguments: ""}}];
	else{
		newElement = [	{ group: "nodes", data: { id: elementId, label: elementLabel, arguments: ""}},
						{ group: "edges", data: { id: edgeID, source: lastElement.id(), target: elementId}} ];
	}
	
	// Can't use cy.add, because a position is needed, which is not available. Workaround: cy.load
	cy.load(oldElements.concat(newElement));
});

/** 
 * Open download dialog for download a zip file that contains the project.json and an script
 */
$('#downloadProject').click( function(){
	var zip = new JSZip();
	
	var data = cy.elements('*').jsons();
	var script = generateScript(extractSequence());
	
	zip.file("project.json",JSON.stringify(data));
	zip.file("script.sh", script, {unixPermissions: "755"}); //, 
	
	var content = zip.generate({type: "blob", platform: "UNIX"});

	saveAs(content, "project.zip");
});

/** 
 * Open FileChooser from hidden element
 */
$('#loadProject').click( function(){
	$('#files').click();
});

/** 
 * Read JSON files, selected by Filechooser and load the content into the canvas
 */
$('#files').on("change", function(){
	var file = document.getElementById('files').files[0];
	var reader = new FileReader();
	var dataType = /json.*/;

	if (file.type.match(dataType)) {
		reader.onload = function() { cy.load(JSON.parse(reader.result)); }
		var content = reader.readAsText(file);
	}
});

/** 
 * TODO Upload Script, run it, download result
 */
$('#runProject').click( function(){
});

/** 
 * Extract all programs and their arguments from the canvas
 * 
 * @return JsonArray(JsonObjects(name: String,args: String))
 */
function extractSequence() {
	var nodes = cy.nodes("*");
	var next = cy.nodes().roots().first();
	
	var seq = [];
	
	while(next.size() != 0){
		seq.push({name: next.data("label"), args: next.data("arguments")});
		var edges = next.edgesTo(nodes);
		next = (edges.size() != 0) ? edges.first().target() : edges; //Hack to return a Datatype with the method size()
	}
	return seq;
}

/** 
 * Generate a bash script from an Array of type JsonArray(JsonObjects(name,args))
 * 
 * @return script: String
 */
function generateScript(seq) {
	var script = "#!/bin/bash\n\n";
	for(i in seq) {
		script += seq[i].name + " " + seq[i].args;
		if(i != seq.length-1) script += "|\n";
	}
	return script;
}