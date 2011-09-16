Ext.canvasXpress = Ext.extend(Ext.Panel, {
  showExampleData: false,
  data: false,
  options: false,
  events: false,
  contextMenu: true,
  canvasId: Ext.id(),
  initComponent: function() {
    Ext.canvasXpress.superclass.initComponent.apply(this, arguments);
    if (! this.data && this.showExampleData) {
      this.data = false;
    } else if (! this.data && !this.showExampleData){
      this.data = {};
    }
  },
  onRender:function() {
    Ext.canvasXpress.superclass.onRender.apply(this, arguments);
    if (this.contextMenu) {
      this.el.on({contextmenu:{scope:this, fn:this.onContextMenu, stopEvent:true}});
    }
  },
  afterRender: function() {
    Ext.canvasXpress.superclass.afterRender.apply(this, arguments);
    // Add the canvas tag
    Ext.DomHelper.append(this.id, {
      tag: 'canvas',
      id: this.canvasId,
      width: this.width || 500,
      height: this.height || 500
    });
    // Get the canvasXpress object
    this.canvas = new CanvasXpress(this.canvasId, this.data, this.options, this.events);
    if (this.canvas.version < 2) {
      var msg = 'Please download a newer version of canvasXpress at:<br>';
      msg += '<a target="blank" src="http://www.canvasXpress.org">http://www.canvasXpress.org</a><br>';
      msg += 'You are using an older version that dO NOT support all the functionality of this panel';
      Ext.MessageBox.alert('Warning', msg);
    }
    if (this.contextMenu) {
      this.contextMenu = new Ext.menu.Menu();
    }    
  },
  onContextMenu: function (e) {
    if (e.browserEvent) {
      if (this.contextMenu) {
	this.contextMenu.destroy();
      }
      this.contextMenu = new Ext.menu.Menu(this.createContextMenu());
      this.contextMenu.showAt(e.getXY());
      e.stopEvent();
    }
  },
  createContextMenu: function () {
    var items = [];
    items.push({
      text:'Customize',
      style:'font-weight:bold;margin:0px 4px 0px 27px;line-height:18px'
    });
    items.push('-');
    this.addItemToMenu(items, 'General', false, this.generalMenus());
    this.addItemToMenu(items, 'Axes', false, this.axesMenus());
    this.addItemToMenu(items, 'Labels', false, this.labelMenus());
    this.addItemToMenu(items, 'Legend', false, this.legendMenus());
    this.addItemToMenu(items, 'Data', false, this.dataMenus());
    items.push('-');
    items.push({text: 'Print',
		iconCls: '',
		canvasId: this.canvasId,
		handler: this.onPrintGraph});
    items.push('-');
    return items;
  },
  // Utilities
  // Add to Menu
  addItemToMenu: function (container, text, icon, items){
    if (!icon) {
      icon = '';
    }
    if (items && items.length > 0) {
      container.push({
	text: text,
	iconCls: icon,
	menu: items
      });
    }
  },
  // Form
  addFormParameter: function (p, isText) {
    var f;
    if (isText) {
      f = new Ext.form.TextField({
	width: 80,
	iconCls: 'no-icon',
	canvasId: this.canvasId,
	name: p,
	value: this.canvas[p] ? this.canvas[p] : '',
	enableKeyEvents: true
      });
    } else {
      f = new Ext.form.NumberField({
	width: 80,
	iconCls: 'no-icon',
	canvasId: this.canvasId,
	name: p,
	value: this.canvas[p] ? this.canvas[p] : '',
	enableKeyEvents: true
      });
    }
    f.on('change', this.addFormEvent, this);
    f.on('specialkey', this.enterFormEvent, this);
    return [f];
  },
  enterFormEvent: function(f, e) {
    if (e.getKey() == e.ENTER) {
      this.addFormEvent(f, f.getValue());
      this.contextMenu.hide();
    }
  },
  addFormEvent: function(f, n, o) {
    var p = f.name;
    var w = p == 'width' ? n : false;
    var h = p == 'height' ? n : false;
    if (f.canvasId) {
      var t = Ext.getCmp(Ext.get(f.canvasId).parent().parent().id);
      if (t.canvas && t.canvas[p] != n) {
	t.canvas[p] = n;
	t.canvas.draw(w, h);
      }
    }	  
  },
  // Color Menu
  colorMenu: function (par) {
    var handler = this.buildColorHandler(par);
    return new Ext.menu.ColorMenu({
      width: 155,
      canvasId: this.canvasId,
      handler: handler
    });
  },
  buildColorHandler: function(par) {
    return function (cm, color) {
      var t = Ext.getCmp(Ext.get(cm.canvasId).parent().parent().id);
      if (t.canvas) {
        t.canvas[par] = t.hexToRgb(color);
        t.canvas.draw();
      }
    }
  },
  hexToRgb: function (color) {
    var r = parseInt(color.substring(0,2),16);
    var g = parseInt(color.substring(2,4),16);
    var b = parseInt(color.substring(4,6),16);
    return 'rgb('+ r + ',' + g + ',' + b + ')';
  },
  // Radio Group
  buildGroupMenu: function(par, vals, callback) {
    var m = [];
    for (var i = 0; i < vals.length; i++) {
      m.push({text: vals[i] ? vals[i] : 'false',
	      group: par,
	      checked: this.canvas[par] == vals[i] ? true : false,
	      canvasId: this.canvasId,
	      checkHandler: callback ? callback : this.clickedGroupMenu
	     });
    }
    return m;
  },
  clickedGroupMenu: function(item) {
    if (item.canvasId) {
      var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
      var p = item.text == 'false' ? false : item.text;
      if (t.canvas && t.canvas[item.group] != p) {
	t.canvas[item.group] = p;
	t.canvas.draw();
      }
    }		  
  },
  // Print
  onPrintGraph: function(item) {
    if (item.canvasId) {
      var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
      if (t.canvas) {
	t.canvas.printExt();
      }
    }		  
  },
  // Legend
  legendMenus: function () {
    if (this.canvas.hasLegend()) {
      var items = [{
	text: 'Show',
	iconCls: '',
	menu: this.buildGroupMenu('showLegend', [true, false])
      }];
      if (this.canvas.hasLegendProperties() && this.canvas.showLegend) {
	items.push({
	  text: 'Position',
	  iconCls: '',
	  menu: this.buildGroupMenu('legendPosition', ['bottom', 'right'])	
	});
	items.push({
	  text: 'Boxed',
	  iconCls: '',
	  menu: this.buildGroupMenu('legendBox', [true, false])
	});
      }
      return items;
    } else {
      return false;
    }
  },
  // Data
  dataMenus: function () {
    if (this.canvas.hasData()) {
      var items = [];
      if (this.canvas.hasDataSamples()) {
	this.addItemToMenu(items, 'Samples', false, this.dataSamples());
      }
      if (this.canvas.hasDataVariables()) {
	this.addItemToMenu(items, 'Variables', false, this.dataVariables());
      }
      if (this.canvas.hasDataGroups() && this.canvas.isGroupedData) {
 	this.addItemToMenu(items, 'Groups', false, this.dataGroups());
      }
      this.addItemToMenu(items, 'Series', false, this.dataSeries());
      if (this.canvas.hasDataProperties()) {
	this.addItemToMenu(items, 'Range', false, this.dataRange());
	this.addItemToMenu(items, 'Transformation', false, this.transformations());
	items.push({
	  text: 'Error Bars',
	  iconCls: '',
	  menu: this.buildGroupMenu('showErrorBars', [true, false])
	})
      }
      return items;
    }
  },
  transformations: function () {
    var s = [];
    var smps = this.canvas.getSamples();
    for (var i = 0; i < smps.length; i++) {
      var check = smps[i].index == this.canvas.ratioReference ? true : false;
      s.push({
	group: 'ratioReference',
	text: smps[i].name,
	index: smps[i].index,
	checked: check,
	canvasId: this.canvasId,
	checkHandler: this.onDataTransform
      });    
    }
    return [{
      text: 'Type',
      iconCls: '',
      menu: this.buildGroupMenu('transformType', ['log2', 'log10', 'exp2', 'exp10', 'percentile', 'zscore', 'ratio', false, 'reset', 'save'], this.onDataTransform)
    }, {
      text: 'Ratio Reference',
      iconCls: '',
      menu: s      
    }, {
      text: 'Z-Score Axis',
      iconCls: '',
      menu: this.buildGroupMenu('zscoreAxis', ['samples', 'variable'], this.onDataTransform)      
    }];
  },
  onDataTransform: function (item) {
    if (item.canvasId) {
      var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
      var p = item.group == 'ratioReference' ? item.index : item.text == 'false' ? false : item.text;
      if (t.canvas && t.canvas[item.group] != p) {
	t.canvas[item.group] = p;
	var tr = t.canvas.transformType;
	var ax = t.canvas.zscoreAxis;
	var id = t.canvas.ratioReference;
	if (tr) {
	  t.canvas.transform(tr, ax, id);
	}
	t.canvas.draw();
      }
    }		  
  },
  dataSamples: function () {
    var s = [];
    var smps = this.canvas.getSamples();
    for (var i = 0; i < smps.length; i++) {
      var check = smps[i].hidden ? false : true;
      s.push({
	text: smps[i].name,
	checked: check,
	canvasId: this.canvasId,
	handler: this.onSamples
      });    
    }
    var g = [];
    var check;
    var annt = this.canvas.getAnnotations();
    var grp = this.canvas.getGroupingFactors();
    for (var i = 0; i < annt.length; i++) {
      check = grp.hasOwnProperty(annt[i]) ? true : false;
      g.push({
	text: annt[i],
	checked: check,
	canvasId: this.canvasId,
	handler: this.onGroup
      });    
    }
    return [{
      text: 'Show',
      iconCls: '',
      menu: s
    }, {
      text: 'Group',
      iconCls: '',
      menu: g
    }, {
      text: 'Sort',
      iconCls: '',
      menu: this.dataSamplesSort()
    }];
  },
  onSamples: function (item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    if (t.canvas) {
      t.canvas.hideUnhideSmps(item.text);
      t.canvas.draw();
    }
  },
  onGroup: function (item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    if (t.canvas) {
      t.canvas.modifyGroupingFactors(item.text, item.checked);
      t.canvas.groupSamples(t.canvas.getGroupingFactors(true));
      t.canvas.draw();
    }
  },
  dataSamplesSort: function () {
    var v = [];
    var vars = this.canvas.getVariables();
    for (var i = 0; i < vars.length; i++) {
      v.push({
	text: vars[i].name,
	isVar: true,
	index: i + 1,
	checked: this.canvas.varSort == i ? true : false,
	canvasId: this.canvasId,
	handler: this.onSamplesSort
      });    
    }
    var c = [];
    var annt = this.canvas.getAnnotations();
    for (var i = 0; i < annt.length; i++) {
      c.push({
	group: 'catSort',
	text: annt[i],
	category: true,
	checked: this.canvas.smpSort == annt[i] ? true : false,
	canvasId: this.canvasId,
	handler: this.onSamplesSort
      });
    }
    return [{
      text: 'By Value of Variable',
      iconCls: '',
      menu: v
    }, {
      text: 'By Annotation',
      iconCls: '',
      menu: c
    }, {
      text: 'By Name',
      iconCls: '',
      canvasId: this.canvasId,
      handler: this.onSamplesSort
    }, {
      text: 'Direction',
      iconCls: '',
      menu: this.buildGroupMenu('sortDir', ['ascending', 'descending']) 
    }];
  },
  onSamplesSort: function (item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    var p = item.text == 'false' ? false : item.text;
    if (t.canvas && t.canvas[item.group] != p) {
      if (item.group == 'sortDir') {
	t.canvas.sortDir = item.text;
      }
      if (item.text == 'By Name') {
	t.canvas.sortIndices('smps', t.canvas.sortDir);
      } else {
	if (item.category) {
	  t.canvas.sortIndices('smps', t.canvas.sortDir, item.text);
	} else if (item.isVar) {
	  t.canvas.sortIndices('smps', t.canvas.sortDir, false, false, item.index);
	} else {
	  t.canvas.sortIndices('smps', t.canvas.sortDir);
	}
      }
      t.canvas.draw();
    }
  },
  dataGroups: function () {
    var g = [];
    var grps = this.canvas.getSamples();
    for (var i = 0; i < grps.length; i++) {
      var check = grps[i].hidden ? false : true;
      g.push({
	text: grps[i].name,
	checked: check,
	isVar: false,
	canvasId: this.canvasId,
	handler: this.onSamples
      });
    }
    return [{
      text: 'Show',
      iconCls: '',
      menu: g
    }];
  },
  dataVariables: function () {
    var items = [];
    var v = [];
    var vars = this.canvas.getVariables();
    var annt = this.canvas.getAnnotations(true);
    for (var i = 0; i < vars.length; i++) {
      var check = vars[i].hidden ? false : true;
      v.push({
	text: vars[i].name,
	checked: check,
	canvasId: this.canvasId,
	handler: this.onVariables
      });    
    }
    items.push({
      text: 'Show',
      iconCls: '',
      menu: v      
    })
    if (annt && this.canvas.isSegregable()) {
      var c = [];
      annt.push(false);
      for (var i = 0; i < annt.length; i++) {
	c.push({
	  group: 'segregateBy',
	  text: annt[i] == false ? 'false' : annt[i],
	  category: true,
	  checked: this.canvas.segregateBy == annt[i] ? true : false,
	  canvasId: this.canvasId,
	  handler: this.onSegregate
	});
      }
      items.push({
	text: 'Segregate',
	iconCls: '',
	menu: c      
      })
      annt.pop();
    }
    items.push({
      text: 'Sort',
      iconCls: '',
      menu: this.dataVariablesSort(annt)      
    })
    return items;
  },
  onVariables: function (item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    if (t.canvas) {
      t.canvas.hideUnhideVars(item.text);
      t.canvas.draw();
    }
  },
  onSegregate: function(item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    if (t.canvas) {
      if (item.text == 'false') {
	t.canvas.desegregateVariables();
      } else {
	t.canvas.segregateVariables(item.text);
      }
      t.canvas.draw();
    }
  },
  dataVariablesSort: function (annt) {
    var items = [];
    var s = [];
    var smps = this.canvas.getSamples();
    for (var i = 0; i < smps.length; i++) {
      s.push({
	group: 'sampleSort',
	text: smps[i].name,
	index: i + 1,
	isSmp: true,
	category: false,
	checked: this.canvas.smpSort == i ? true : false,
	canvasId: this.canvasId,
	handler: this.onVariablesSort
      });    
    }
    var c = [];
    if (annt) {
      for (var i = 0; i < annt.length; i++) {
	c.push({
	  group: 'catSort',
	  text: annt[i],
	  category: true,
	  checked: this.canvas.varSort == annt[i] ? true : false,
	  canvasId: this.canvasId,
	  handler: this.onVariablesSort
	});
      }
    }
    items.push({
      text: 'By Value in Samples',
      iconCls: '',
      menu: s
    })
    if (c.length > 0) {
      items.push({
	text: 'By Annotation',
	iconCls: '',
	menu: c
      })
    }
    items.push({
      text: 'By Name',
      iconCls: '',
      canvasId: this.canvasId,
      handler: this.onVariablesSort
    })
    items.push({
      text: 'Direction',
      iconCls: '',
      menu: this.buildGroupMenu('sortDir', ['ascending', 'descending']) 
    });
    return items;
  },
  onVariablesSort: function (item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    var p = item.text == 'false' ? false : item.text;
    if (t.canvas && t.canvas[item.group] != p) {
      if (item.group == 'sortDir') {
	t.canvas.sortDir = item.text;
      }
      if (item.text == 'By Name') {
	t.canvas.sortIndices('vars', t.canvas.sortDir);
      } else {
	if (item.category) {
	  t.canvas.sortIndices('vars', t.canvas.sortDir, item.text);
	} else if (item.isSmp) {
	  t.canvas.sortIndices('vars', t.canvas.sortDir, false, item.index);
	} else {
	  t.canvas.sortIndices('vars', t.canvas.sortDir);
	}
      }
      t.canvas.draw();
    }
  },
  dataRange: function () {
    var m = [];
    var axes = this.canvas.getValidAxes();
    for (var i = 0; i < axes.length; i++) {
      var l = axes[i] == 'xAxis2' ? 'X2' : axes[i].substring(0,1).toUpperCase();
      var p = this.canvas.graphType.match(/Scatter/) ? l : axes[i] == 'xAxis2' ? '2' : '';
      m.push({text: axes[i].replace('Axis', '-Axis'),
	      iconCls: '',
	      menu: [{text: 'Min',
		      menu: this.addFormParameter('setMin' + p)},
		     {text: 'Max',
		      menu: this.addFormParameter('setMin' + p)}]});
    }
    return m;
  },
  dataSeries: function () {
    var m = [];
    if (this.canvas.graphType == 'BarLine' || this.canvas.graphType.match(/Scatter/) || this.canvas.graphType == 'Pie') {
      var axes = this.canvas.getValidAxes(true);
      var objs = this.canvas.graphType == 'BarLine' ? this.canvas.getVariables() : this.canvas.getSamples();
      for (var i = 0; i < axes.length; i++) {
	var oba = this.canvas.graphType == 'BarLine' ? this.canvas.getVariablesByAxis(axes[i]) : this.canvas.getSamplesByAxis(axes[i]);
	var o = [];
	for (var j = 0; j < objs.length; j++) {
	  var check = false;
	  for (var k = 0; k < oba.length; k++) {
	    if (objs[j].name == oba[k]) {
	      check = true;
	      break;
	    }
	  }
	  if (this.canvas.graphType == 'Scatter2D') {
	    o.push({text: objs[j].name,
		    checked: check,
		    canvasId: this.canvasId,
		    isVar: false,
		    handler: this.onDataSeries});
	  } else if (this.canvas.graphType == 'ScatterBubble2D' || this.canvas.graphType == 'Scatter3D' || this.canvas.graphType == 'Pie') {
	    o.push({text: objs[j].name,
		    group: 'axis',
		    checked: check,
		    canvasId: this.canvasId,
		    isVar: false,
		    checkHandler: this.onDataSeries});
	  } else if (this.canvas.graphType == 'BarLine') {
	    o.push({text: objs[j].name,
		    checked: check,
		    canvasId: this.canvasId,
		    isVar: true,
		    handler: this.onDataSeries});
	  }
	}
	var str = axes[i].replace('Axis', '-Axis');
	if (o.length > 0) {
	  m.push({text: str,
		  iconCls: '',
		  menu: o});
	}
      }
    }
    return m;
  },
  onDataSeries: function (item) {
    var axis = item.getBubbleTarget().ownerCt.text;
    var check = item.checked;
    var isVar = item.isVar;
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    if (t.canvas) {
      axis = axis.replace('-Axis', 'Axis');
      var res;
      if (isVar) {
	res = t.canvas.addRemoveVariablesInAxis(item.text, axis, check);
      } else {
	res = t.canvas.addRemoveSamplesInAxis(item.text, axis, check);
      }
      if (res) {
	Ext.MessageBox.alert('Status', res);
      } else {
	t.canvas.draw();
      }
    }
  },
  // Labels
  labelMenus: function () {
    var items = [];
    var lab = this.canvas.isGroupedData ? 'Groups' : 'Samples';
    if (this.canvas.hasDataSamples()) {
      this.addItemToMenu(items, lab, false, this.labelSamples());
    }
    if (this.canvas.hasDataVariables()) {
      this.addItemToMenu(items, 'Variables', false, this.labelVariables());
    }
    return items;
  },
  labelSamples: function () {
    var h = [];
    var smps = this.canvas.getSamples();
    var hls = this.canvas.getHighlights();
    for (var i = 0; i < smps.length; i++) {
      var check = hls.hasOwnProperty(smps[i].name) ? true : false;
      h.push({
	text: smps[i].name,
	checked: check,
	isVar: false,
	canvasId: this.canvasId,
	handler: this.onHighlight
      });
    }
    return [{
      text: 'Max size',
      iconCls: '',
      canvasId: this.canvasId,
      menu: this.addFormParameter('maxSmpStringLen')
    }, {
      text: 'Colors',
      iconCls: '',
      menu: this.colorMenu('smpLabelColor')
    }, {
      text: 'Highlight',
      iconCls: '',
      menu: h
    }, {
      text: 'Highlight Color',
      iconCls: '',
      menu: this.colorMenu('smpHighlightColor')
    }];
  },
  labelVariables: function () {
    var h = [];
    var vars = this.canvas.getVariables();
    var hls = this.canvas.getHighlights(true);
    for (var i = 0; i < vars.length; i++) {
      var check = hls.hasOwnProperty(vars[i].name) ? true : false;
      h.push({
	text: vars[i].name,
	checked: check,
	isVar: true,
	canvasId: this.canvasId,
	handler: this.onHighlight
      });
    }
    return [{
      text: 'Max size',
      iconCls: '',
      menu: this.addFormParameter('maxVarStringLen')
    }, {
      text: 'Colors',
      iconCls: '',
      menu: this.colorMenu('varLabelColor')
    }, {
      text: 'Highlight',
      iconCls: '',
      menu: h
    }, {
      text: 'Highlight Color',
      iconCls: '',
      menu: this.colorMenu('varHighlightColor')
    }];
  },
  onHighlight: function (item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    if (t.canvas) {
      t.canvas.modifyHighlights(item.text, item.checked, item.isVar);
      t.canvas.draw();
    }
  },
  // Axes
  axesMenus: function () {
    var items = [];
    var axes = this.canvas.getValidAxes();
    if (axes) {
      this.addItemToMenu(items, 'Colors', false, this.axesColors(axes));
      this.addItemToMenu(items, 'Show', false, this.axesShow(axes));
      this.addItemToMenu(items, 'Properties', false, this.axesProperties(axes));
      this.addItemToMenu(items, 'Titles', false, this.axesTitles(axes));
      this.addItemToMenu(items, 'Ticks', false, this.axesTicks(axes));
      this.addItemToMenu(items, 'Transform', false, this.axesTransforms(axes));
    }
    return items;
  },
  axesColors: function (axes) {
    var m = [];
    var ax = [];
    for (var i = 0; i < axes.length; i++) {
      var s = axes[i].replace('Axis', '-Axis');
      ax.push({text: s,
	       iconCls: '',
	       menu: this.colorMenu(axes[i] + 'TickColor')});
    }
    if (ax.length > 0) {
      m.push({text: 'Mesh',
	      iconCls: '',
	      menu: ax});
    }
    m.push({text: 'Titles',
	    iconCls: '',
	    menu: this.colorMenu('axisTitleColor')});    
    m.push({text: 'Values',
	    iconCls: '',
	    menu: this.colorMenu('axisTickColor')});    
    return m;
  },
  axesShow: function (axes) {
    var m = [];
    for (var i = 0; i < axes.length; i++) {
      m.push({text: axes[i].replace('Axis', '-Axis'),
	      iconCls: '',
	      menu: this.buildGroupMenu(axes[i] + 'Show', [true, false])});
    }
    return m;
  },
  axesProperties: function (axes) {
    var m = [];
    var a = [];
    m.push({text: 'Over-Extension',
	    iconCls: '',
	    menu: this.addFormParameter('axisExtension')});
    for (var i = 0; i < axes.length; i++) {
      a.push({text: axes[i].replace('Axis', '-Axis'),
	      iconCls: '',
	      menu: this.buildGroupMenu(axes[i] + 'Exact', [true, false])});
    }
    if (a.length > 0) {
      m.push({text: 'Exact',
	      iconCls: '',
	      menu: a});
    }
    return m;
  },
  axesTitles: function (axes) {
    var m = [];
    if (this.canvas.graphType.match(/Scatter/)) {
      for (var i = 0; i < axes.length; i++) {
	var s = axes[i].replace('Axis', '-Axis');
	var n = axes[i] + 'Title';
	m.push({text: s,
		iconCls: '',
		menu: this.addFormParameter(n, true)});
      }
    }
    return m;
  },
  axesTicks: function (axes) {
    var m = [];
    var n = this.axesTicksNumber(axes);
    var s = this.axesTickStyles(axes);
    if (n.length > 0) {
      m.push({text: 'Number',
              iconCls: '',
              menu: n});
    }
    if (s.length > 0) {
      m.push({text: 'Style',
	      iconCls: '',
	      menu: s});
    }
    return m;
  },
  axesTicksNumber: function (axes) {
    var m = [];
    for (var i = 0; i < axes.length; i++) {
      var s = axes[i].replace('Axis', '-Axis');
      var n = axes[i] + 'Ticks';
      m.push({text: s,
	      iconCls: '',
	      menu: this.addFormParameter(n)});
    }
    return m;
  },
  axesTickStyles: function (axes) {
    var m = [];
    if (this.canvas.graphType.match(/Scatter/)) {
      for (var i = 0; i < axes.length; i++) {
	var s = axes[i].replace('Axis', '-Axis');
	var n = axes[i] + 'TickStyle';
	m.push({text: s,
		iconCls: '',
		menu: this.buildGroupMenu(n, ['solid', 'dotted'])});
      }
    }
    return m;
  },
  axesTransforms: function (axes) {
    var m = [];
    if (this.canvas.graphType.match(/Scatter/)) {
      for (var i = 0; i < axes.length; i++) {
	m.push({text: axes[i].replace('Axis', '-Axis'),
		iconCls: '',
		menu: this.buildGroupMenu(axes[i] + 'Transform', ['log2', 'log10', 'exp2', 'exp10',
								  'percentile', false])});
      }
    }
    return m;
  },
  // General
  generalMenus: function () {
    var items = [];
    this.addItemToMenu(items, 'Colors', false, this.generalColors());
    this.addItemToMenu(items, 'Dimensions', false, this.dimensions());
    this.addItemToMenu(items, 'Fonts', false, this.font());
    this.addItemToMenu(items, 'Layout', false, this.graphLayout());
    this.addItemToMenu(items, 'Main Title', false, this.titles());
    this.addItemToMenu(items, 'Orientation', false, this.graphOrientation());
    this.addItemToMenu(items, 'Overlays', false, this.overlays());    
    this.addItemToMenu(items, 'Plot', false, this.plot());
    this.addItemToMenu(items, 'Shadows', false, this.shadows());
    this.addItemToMenu(items, 'Types', false, this.graphTypes());
    return items;
  },
  generalColors: function() {
    return [{
      text: 'Background',
      iconCls: '',
      menu: this.background()
    }, {
      text: 'Foreground',
      iconCls: '',
      menu: this.foreground()
    }];
  },
  foreground: function () {
    return this.colorMenu('foreground');
  },
  background: function () {
    var m = [];
    m.push({text: 'Type',
	    iconCls: '',
	    menu: this.backgroundType()});    
    if (this.canvas.backgroundType == 'gradient') {
      m.push({text: 'Colors',
	      iconCls: '',
	      menu: this.backgroundGradients()});    
    } else {
      m.push({text: 'Color',
	      iconCls: '',
	      menu: this.colorMenu('background')});    
    }
    return m;
  },
  backgroundType: function () {
    return this.buildGroupMenu('backgroundType', ['solid', 'gradient']);
  },
  backgroundGradients: function () {
    return [{
      text: 'Color1',
      iconCls: '',
      menu: this.colorMenu('backgroundGradient1Color')
    }, {
      text: 'Color2',
      iconCls: '',
      menu: this.colorMenu('backgroundGradient2Color')
    }];
  },
  dimensions: function () {
    return [{
      text: 'Width',
      iconCls: '',
      menu: this.addFormParameter('width')
    }, {
      text: 'Height',
      iconCls: '',
      menu: this.addFormParameter('height')
    }, {
      text: 'Margins',
      iconCls: '',
      menu: this.addFormParameter('margin')
    }];
  },
  font: function() {
    return [
      {text: 'Name',
       iconCls: '',
       menu: this.fontName()},
      {text: 'Max Size',
       iconCls: '',
       menu: this.fontSize()}
    ];
  },
  fontName: function() {
    return this.buildGroupMenu('fontName', this.canvas.fonts);
  },
  fontSize: function() {
    return this.buildGroupMenu('maxTextSize', [8, 9, 10, 11, 12, 13, 14, 15, 16]);
  },
  graphLayout: function() {
    var items = [];
    if (this.canvas.layoutComb) {
      var c = 0;
      for (var i = 0; i < this.canvas.layoutRows; i++) {
	for (var j = 0; j < this.canvas.layoutCols; j++) {
	  var lab = 'Graph ' + (c + 1) + ' Weight';
	  items.push({
	    text: lab,
	    iconCls: '',
	    menu: this.addFormParameter('subGraphWeight' + c)
	  });
	  c++;
	}
      }
    }
    return items;
  },
  titles: function () {
    var items = [{
      text: 'Title',
      iconCls: '',
      menu: this.titlesTitle()
    }];
    if (this.canvas.title) {
      items.push({
	text: 'Subtitle',
	iconCls: '',
	menu: this.titlesSubTitle()
      });
    }
    return items;
  },
  titlesTitle: function () {
    var items = [{
      text: 'Text',
      iconCls: '',
      menu: this.addFormParameter('title', true)
    }];
    if (this.canvas.title) {
      items.push({
	text: 'Height',
	iconCls: '',
	menu: this.addFormParameter('titleHeight')
      });
    }
    return items;
  },
  titlesSubTitle: function () {
    var items = [{
      text: 'Text',
      iconCls: '',
      menu: this.addFormParameter('subtitle', true)
    }];
    if (this.canvas.subtitle) {
      items.push({
	text: 'Height',
	iconCls: '',
	menu: this.addFormParameter('subtitleHeight')
      });
    }
    return items;
  },
  graphOrientation: function () {
    if (this.canvas.hasOrientation()) {
      return this.buildGroupMenu('graphOrientation', ['vertical', 'horizontal']);
    } else {
      return false;
    }
  },
  overlays: function () {
    if (this.canvas.hasOrientation()) {
      var items = [];
      var o = [];
      var annt = this.canvas.getAnnotations();
      var ovls = this.canvas.getOverlays();
      var isOvl = false;
      for (var i = 0; i < annt.length; i++) {
	var check = ovls.hasOwnProperty(annt[i]) ? true : false;
	if (check) {
	  isOvl = true;
	}
	o.push({
	  text: annt[i],
	  checked: check,
	  canvasId: this.canvasId,
	  handler: this.onOverlays
	});
      }
      if (annt && annt.length > 0) {
	items.push({
	  text: 'Annotations',
	  iconCls: '',
	  menu: o   
	});
	if (isOvl) {
	  items.push({
	    text: 'Width',
	    iconCls: '',
	    menu: this.addFormParameter('overlaysWidth')   
	  });
	}
      }
      return items;
    } else {
      return false;
    }

  },
  onOverlays: function (item) {
    var t = Ext.getCmp(Ext.get(item.canvasId).parent().parent().id);
    if (t.canvas) {
      t.canvas.modifyOverlays(item.text, item.checked);
      t.canvas.draw();
    }    
  },
  // Plot specific
  plot: function () {
    switch (this.canvas.graphType) {
      case 'Bar':
      case 'Area':
      case 'Dotplot':
      case 'Stacked':
      case 'StackedPercent':
      case 'Boxplot':
        return this.oneDGraphs();
      case 'Line':
      case 'BarLine':
        return [{
	  text: 'Configuration',
	  iconCls: '',
	  menu: this.oneDGraphs()
	}, {
	  text: 'Lines',
	  iconCls: '',
	  menu: this.lines() 
	}];
      case 'Heatmap':
        var items = [{
	  text: 'Configuration',
	  iconCls: '',
	  menu: this.oneDGraphs()
	}, {
	  text: 'Heatmaps',
	  iconCls: '',
	  menu: this.heatmaps()
	}];
        this.addItemToMenu(items, 'Trees', false, this.dendrograms())
        return items;
      case 'Scatter2D':
        return this.scatter2D();
      case 'ScatterBubble2D':
        return false;
      case 'Scatter3D':
        return this.scatter3D();
      case 'Correlation':
        return this.correlation();
      case 'Venn':
        return this.venn();
      case 'Pie':
        return this.pie();
      case 'Network':
        return this.networks();
      case 'Genome':
      return this.genomeBrowser();
    }
  },
  shadows: function () {
    if (!this.canvas.isIE) {
      return [{
	text: 'Show',
	iconCls: '',
	menu: this.buildGroupMenu('showShadow', [true, false])
      }];
    } else {
      return false;
    }
  },
  // 1D graaphs
  oneDGraphs: function () {
    if (this.canvas.graphType == 'Heatmap') {
      return this.blocks()
    } else {
      var items = [];
      this.addItemToMenu(items, 'Sample Hairline', false, this.hairline());
      this.addItemToMenu(items, 'Sample Blocks', false, this.blocks());
      this.addItemToMenu(items, 'Trees', false, this.dendrograms(true))
      return items;
    }
  },
  hairline: function () {
    return [{
      text: 'Type',
      iconCls: '',
      menu: this.buildGroupMenu('smpHairline', ['solid', 'dotted', false])
    }, {
      text: 'Color',
      iconCls: '',
      menu: this.colorMenu('smpHairlineColor')
    }];
  },
  blocks: function() {
    var items = [];
    if (this.canvas.graphType != 'Heatmap') {
      items.push({
	text: 'Contrast',
	iconCls: '',
	menu: this.buildGroupMenu('blockContrast', [true, false])
      });
      if (this.canvas.blockContrast) {
	items.push({
	  text: 'Contrast Odd Colors',
	  iconCls: '',
	  menu: this.colorMenu('blockContrastOddColor')
	});
	items.push({
	  text: 'Contrast Even Colors',
	  iconCls: '',
	  menu: this.colorMenu('blockContrastEvenColor')
	});
      }
    }
    if (! this.canvas.layoutComb) {
      items.push({
	text: 'Autoextend',
	iconCls: '',
	menu: this.buildGroupMenu('autoExtend', [true, false])
      });
      if (this.canvas.autoExtend) {
	if (this.canvas.graphType != 'Heatmap') {
	  items.push({
	    text: 'Separation Factor',
	    iconCls: '',
	    menu: this.buildGroupMenu('blockSeparationFactor', [0, 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 3, 4, 5])
	  });
	}
	items.push({
	  text: 'Width / Height Factor',
	  iconCls: '',
	  menu: this.buildGroupMenu('blockFactor', [0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 3, 4, 5])
	});
      }
    }
    return items;
  },
  // Lines / BarLines
  lines: function () {
    var items = [{
      text: 'Decorations',
      iconCls: '',
      menu: this.buildGroupMenu('lineDecoration', ['dot', 'symbol', false])      
    }]
    if (this.canvas.graphType != 'Barline') {
      items.push({
	text: 'Coordinate Colors in Bar / Lines',
	iconCls: '',
	menu: this.buildGroupMenu('coordinateLineColor', [true, false])	
      })
    }
    return items;
  },
  // 2D Scatter
  scatter2D: function () {
    var items = [{
      text: 'Plot as bars',
      iconCls: '',
      menu: this.buildGroupMenu('barPlot', [true, false])
    }];
    if (this.canvas.barPlot) {
      items.push({
	text: 'Bar width',
	iconCls: '',
	menu: this.addFormParameter('barPlotWidth')
      });
    }
    if (this.canvas.hasDecorations()) {
      items.push({
	text: 'Decorations',
	iconCls: '',
	menu: this.scatter2DDecorations()
      });
    }
    return items;
  },
  scatter2DDecorations: function () {
    var items = [{
      text: 'Show',
      iconCls: '',
      menu: this.buildGroupMenu('showDecorations', [true, false])     
    }];
    if (this.canvas.showDecorations) {
      items.push({
	text: 'Position',
	iconCls: '',
	menu: this.buildGroupMenu('decorationsPosition', ['right', 'bottom'])
      });
      items.push({
	text: 'Color',
	iconCls: '',
	menu: this.colorMenu('decorationsColor')
      })
    }
    return items;
  },
  // 3D Scatter
  scatter3D: function () {
    return [{
      text: 'Rotation',
      iconCls: '',
      menu: this.scatter3DRotate()
    }, {
      text: 'Configuration',
      iconCls: '',
      menu: this.scatter3DParameters()
    }];
  },
  scatter3DParameters: function () {
    var annt = this.canvas.getAnnotations(true);
    annt.push(false);
    return [{
      text: 'Color By',
      iconCls: '',
      menu: this.buildGroupMenu('colorBy', annt)
    }, {
      text: 'Shape By',
      iconCls: '',
      menu: this.buildGroupMenu('shapeBy', annt)
    }, {
      text: 'Size By',
      iconCls: '',
      menu: this.buildGroupMenu('sizeBy', annt)
    }];
  },
  scatter3DRotate: function () {
    return [{
      text: 'X-Axis',
      iconCls: '',
      menu: this.addFormParameter('xRotate')
    }, {
      text: 'Y-Axis',
      iconCls: '',
      menu: this.addFormParameter('yRotate')
    }, {
      text: 'Z-Axis',
      iconCls: '',
      menu: this.addFormParameter('zRotate')
    }];
  },
  // Correlation
  correlation: function () {
    return [{
      text: 'Anchor',
      iconCls: '',
      menu: this.correlationAnchor()
    }, {
      text: 'Axis',
      iconCls: '',
      menu: this.buildGroupMenu('correlationAxis', ['samples', 'variables'])
    }, {
      text: 'Heatmap',
      iconCls: '',
      menu: this.heatmaps()
    }];    
  },
  correlationAnchor: function () {
    return [{
      text: 'Show',
      iconCls: '',
      menu: this.buildGroupMenu('correlationAnchorLegend', [true, false])
    }, {
      text: 'Width',
      iconCls: '',
      menu: this.addFormParameter('correlationAnchorLegendAlignWidth')
    }];
  },
  // Heatmap
  heatmaps: function () {
    return [{
      text: 'Bins',
      iconCls: '',
      menu: this.addFormParameter('bins')
    }, {
      text: 'Indicator Width',
      iconCls: '',
      menu: this.addFormParameter('indicatorWidth')
    }, {
      text: 'Scheme',
      iconCls: '',
      menu: this.buildGroupMenu('heatmapType', ['blue', 'blue-green', 'blue-red', 'green', 'green-blue', 'green-red', 'red', 'red-blue', 'red-green'])
    }];
  },
  // Trees
  dendrograms: function (justSamples) {
    if (this.canvas.hasDendrograms) {
      var items = [];
      items.push({
	text: 'Samples',
	iconCls: '',
	menu: this.dendrogramSamples()
      });
      if (!justSamples) {
	items.push({
	  text: 'Variables',
	  iconCls: '',
	  menu: this.dendrogramVariables()
	});
      }
      if (this.canvas.showSmpDendrogram || this.canvas.showVarDendrogram) {
	items.push({
	  text: 'Height',
	  iconCls: '',
	  menu: this.addFormParameter('dendrogramSpace')
	});
	items.push({
	  text: 'Hang',
	  iconCls: '',
	  menu: this.buildGroupMenu('dendrogramHang', [true, false])
	});
      }
      return items;
    } else {
      return false;
    }
  },
  dendrogramSamples: function () {
    var items = [{
      text: 'Show',
      iconCls: '',
      menu: this.buildGroupMenu('showSmpDendrogram', [true, false])
    }];
    if (this.canvas.showSmpDendrogram) {
      var o = this.canvas.graphOrientation == 'vertical' ? ['top', 'bottom'] : ['left', 'right'];
      items.push({
	text: 'Position',
	iconCls: '',
	menu: this.buildGroupMenu('smpDendrogramPosition', o)
      });
    }
    return items;
  },
  dendrogramVariables: function () {
    var items = [{
      text: 'Show',
      iconCls: '',
      menu: this.buildGroupMenu('showVarDendrogram', [true, false])
    }];
    if (this.canvas.showVarDendrogram) {
      items.push({
	text: 'Position',
	iconCls: '',
	menu: this.buildGroupMenu('varDendrogramPosition', ['top', 'bottom'])
      });
    }
    return items;
  },
  // Venn Diagram
  venn: function () {
    return [{
      text: 'Groups',
      iconCls: '',
      menu: this.buildGroupMenu('vennGroups', [1, 2, 3, 4])
    }]
  },
  // Pie graphs
  pie: function () {
    return [{
      text: 'Style',
      iconCls: '',
      menu: this.buildGroupMenu('pieType', ['solid', 'separated'])
    }, {
      text: 'Precision',
      iconCls: '',
      menu: this.addFormParameter('pieSegmentPrecision')
    }, {
      text: 'Separation',
      iconCls: '',
      menu: this.addFormParameter('pieSegmentSeparation')
    }, {
      text: 'Labels',
      iconCls: '',
      menu: this.buildGroupMenu('pieSegmentLabels', ['inside', 'outside'])
    }];
  },
  // Networks
  networks: function() {
    return [{
      text: 'Animation',
      iconCls: '',
      menu: this.networkAnimation()
    }, {
      text: 'Pre-Scale',
      iconCls: '',
      menu: this.buildGroupMenu('preScaleNetwork', [true, false])
    }, {
      text: 'Nodes',
      iconCls: '',
      menu: this.networkNodes()
    }, {
      text: 'Random Networks',
      iconCls: '',
      menu: this.networkRandom()
    }];
  },
  networkAnimation: function () {
    return [{
      text: 'Show',
      iconCls: '',
      menu: this.buildGroupMenu('showAnimation', [true, false])
    }, {
      text: 'Font',
      iconCls: '',
      menu: this.networkAnimationFont()
    }];
  },
  networkAnimationFont: function () {
    return [{
      text: 'Size',
      iconCls: '',
      menu: this.addFormParameter('showAnimationFontSize')
    }, {
      text: 'Color',
      iconCls: '',
      menu: this.colorMenu('showAnimationFontColor')
    }];
  },
  networkNodes: function () {
    return [{
      text: 'Threshold Number',
      iconCls: '',
      menu: this.addFormParameter('showNodeNameThreshold')
    }, {
      text: 'Font',
      iconCls: '',
      menu: this.networkNodesFont()
    }];
  },
  networkNodesFont: function () {
    return [{
      text: 'Size',
      iconCls: '',
      menu: this.addFormParameter('nodeFontSize')
    }, {
      text: 'Color',
      iconCls: '',
      menu: this.colorMenu('nodeFontColor')
    }];
  },
  networkRandom: function () {
    return [{
      text: 'Generate',
      iconCls: '',
      menu: this.buildGroupMenu('randomNetwork', [true, false])
    }, {
      text: 'Node Number',
      iconCls: '',
      menu: this.addFormParameter('randomNetworkNodes')
    }, {
      text: 'Max Edges / Node',
      iconCls: '',
      menu: this.addFormParameter('randomNetworkNodeEdgesMax')
    }];
  },
  // Genome Browser
  genomeBrowser: function() {
    return [{
      text: 'Tracks',
      iconCls: '',
      menu: this.genomeBrowserTracks()
    }, {
      text: 'Features',
      iconCls: '',
      menu: this.genomeBrowserFeatures() 
    }, {
      text: 'Sequence',
      iconCls: '',
      menu: this.genomeBrowserSequence()
    }, {
      text: 'Wire Color',
      iconCls: '',
      menu: this.colorMenu('wireColor')
    }, {
      text: 'Ticks',
      iconCls: '',
      menu: this.genomeBrowserTicks()
    }];
  },
  genomeBrowserTracks: function() {
    return [{
      text: 'Font',
      iconCls: '',
      menu: this.genomeBrowserTracksFont()
    }];
  },
  genomeBrowserTracksFont: function() {
    return [{
      text: 'Size',
      iconCls: '',
      menu: this.addFormParameter('trackNameFontSize')
    }, {
      text: 'Color',
      iconCls: '',
      menu: this.colorMenu('trackNameFontColor')
    }];
  },
  genomeBrowserFeatures: function() {
    return [{
      text: 'Threshold Number',
      iconCls: '',
      menu: this.addFormParameter('showFeatureNameThereshold')
    }, {
      text: 'Font',
      iconCls: '',
      menu: this.genomeBrowserFeaturesFont()
    }, {
      text: 'Defaults',
      iconCls: '',
      menu: this.genomeBrowserFeaturesDefaults()
    }];
  },
  genomeBrowserFeaturesFont: function() {
    return [{
      text: 'Size',
      iconCls: '',
      menu: this.addFormParameter('featureNameFontSize')
    }, {
      text: 'Color',
      iconCls: '',
      menu: this.colorMenu('featureNameFontColor')
    }];
  },
  genomeBrowserFeaturesDefaults: function() {
    return [{
      text: 'Width',
      iconCls: '',
      menu: this.addFormParameter('featureWidthDefault')
    }, {
      text: 'Height',
      iconCls: '',
      menu: this.addFormParameter('featureHeightDefault')
    }, {
      text: 'Type',
      iconCls: '',
      menu: this.addFormParameter('featureTypeDefault', true)
    }];
  },
  genomeBrowserSequence: function() {
    return [{
      text: 'Font Size',
      iconCls: '',
      menu: this.addFormParameter('sequenceFontSize')
    }, {
      text: 'A Color',
      iconCls: '',
      menu: this.colorMenu('sequenceAColor')
    }, {
      text: 'C Color',
      iconCls: '',
      menu: this.colorMenu('sequenceCColor')
    }, {
      text: 'G Color',
      iconCls: '',
      menu: this.colorMenu('sequenceGColor')
    }, {
      text: 'T Color',
      iconCls: '',
      menu: this.colorMenu('sequenceTColor')
    }, {
      text: 'Multiple Color',
      iconCls: '',
      menu: this.colorMenu('sequenceMColor')
    }];
  },
  genomeBrowserTicks: function() {
    return [{
      text: 'Number',
      iconCls: '',
      menu: this.addFormParameter('ticks')
    }, {
      text: 'Label Periodicity',
      iconCls: '',
      menu: this.addFormParameter('periodTicksLabels')
    }];
  },
  graphTypes: function () {
    if (this.canvas.layoutComb) {
      var items = [];
      var c = 0;
      for (var i = 0; i < this.canvas.layoutRows; i++) {
	for (var j = 0; j < this.canvas.layoutCols; j++) {
	  var lab = 'Graph ' + (c + 1);
	  items.push({
	    text: lab,
	    iconCls: '',
	    menu: this.buildGroupMenu('subGraphType' + c, this.canvas.getValidGraphTypes())
	  });
	  c++;
	}
      }
      return items;
    } else {
      return this.buildGroupMenu('graphType', this.canvas.getValidGraphTypes());
    }
  }
});
Ext.reg('canvasxpress', Ext.canvasXpress);