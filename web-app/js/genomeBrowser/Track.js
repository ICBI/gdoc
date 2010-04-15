function Track(name, key, loaded, changeCallback) {
    this.name = name;
    this.key = key;
    this.loaded = loaded;
    this.changed = changeCallback;
    this.empty = false;
}

Track.prototype.load = function(url) {
    var curTrack = this;
    dojo.xhrGet({url: url,
                 handleAs: "json",
                 load: function(o) { curTrack.loadSuccess(o); },
                 error: function(o) { curTrack.loadFail(o); }
	        });
};

Track.prototype.loadFail = function(error) {
    this.empty = true;
    this.setLoaded();
};

Track.prototype.setViewInfo = function(numBlocks, trackDiv, labelDiv,
                                       widthPct, widthPx, scale) {
    this.div = trackDiv;
    this.label = labelDiv;
    this.widthPct = widthPct;
    this.widthPx = widthPx;

    this.leftBlank = document.createElement("div");
    this.leftBlank.className = "blank-block";
    this.rightBlank = document.createElement("div");
    this.rightBlank.className = "blank-block";
    this.div.appendChild(this.rightBlank);
    this.div.appendChild(this.leftBlank);

    this.sizeInit(numBlocks, widthPct);
    this.labelHTML = "";
    this.labelHeight = 0;
};

Track.prototype.initBlocks = function() {
    this.blocks = new Array(this.numBlocks);
    this.blockHeights = new Array(this.numBlocks);
    for (var i = 0; i < this.numBlocks; i++) this.blockHeights[i] = 0;
    this.firstAttached = null;
    this.lastAttached = null;
    this._adjustBlanks();
};

Track.prototype.clear = function() {
    if (this.blocks) {
        for (var i = 0; i < this.numBlocks; i++)
            this._hideBlock(i);
    }
    this.initBlocks();
};

Track.prototype.setLabel = function(newHTML) {
    if (this.label === undefined) return;

    if (this.labelHTML == newHTML) return;
    this.labelHTML = newHTML;
    this.label.innerHTML = newHTML;
    this.labelHeight = this.label.offsetHeight;
};

Track.prototype.transfer = function() {};

Track.prototype.startZoom = function(destScale, destStart, destEnd) {};
Track.prototype.endZoom = function(destScale, destBlockBases) {};

Track.prototype.showRange = function(first, last, startBase, bpPerBlock, scale) {
    if (this.blocks === undefined) return 0;

    // this might make more sense in setViewInfo, but the label element
    // isn't in the DOM tree yet at that point
    if ((this.labelHeight == 0) && this.label)
        this.labelHeight = this.label.offsetHeight;

    var firstAttached = (null == this.firstAttached ? last + 1 : this.firstAttached);
    var lastAttached =  (null == this.lastAttached ? first - 1 : this.lastAttached);

    var i, leftBase;
    var maxHeight = 0;
    //fill left, including existing blocks (to get their heights)
    for (i = lastAttached; i >= first; i--) {
        leftBase = startBase + (bpPerBlock * (i - first));
        maxHeight = Math.max(maxHeight,
                             this._showBlock(i, leftBase,
                                             leftBase + bpPerBlock, scale));
    }
    //fill right
    for (i = lastAttached + 1; i <= last; i++) {
        leftBase = startBase + (bpPerBlock * (i - first));
        maxHeight = Math.max(maxHeight,
                             this._showBlock(i, leftBase,
                                             leftBase + bpPerBlock, scale));
    }

    //detach left blocks
    var destBlock = this.blocks[first];
    for (i = firstAttached; i < first; i++) {
        this.transfer(this.blocks[i], destBlock);
        this._hideBlock(i);
    }
    //detach right blocks
    destBlock = this.blocks[last];
    for (i = lastAttached; i > last; i--) {
        this.transfer(this.blocks[i], destBlock);
        this._hideBlock(i);
    }

    this.firstAttached = first;
    this.lastAttached = last;
    this._adjustBlanks();

    return Math.max(maxHeight, this.labelHeight ? this.labelHeight : 0);
};

Track.prototype._hideBlock = function(blockIndex) {
    if (this.blocks[blockIndex]) {
        this.div.removeChild(this.blocks[blockIndex]);
        this.blocks[blockIndex] = undefined;
        this.blockHeights[blockIndex] = 0;
    }
};

Track.prototype._adjustBlanks = function() {
    if ((this.firstAttached === null)
        || (this.lastAttached === null)) {
        this.leftBlank.style.left = "0px";
        this.leftBlank.style.width = "50%";
        this.rightBlank.style.left = "50%";
        this.rightBlank.style.width = "50%";
    } else {
        this.leftBlank.style.width = (this.firstAttached * this.widthPct) + "%";
        this.rightBlank.style.left = ((this.lastAttached + 1)
                                      * this.widthPct) + "%";
        this.rightBlank.style.width = ((this.numBlocks - this.lastAttached - 1)
                                       * this.widthPct) + "%";
    }
};

Track.prototype.hideAll = function() {
    if (null == this.firstAttached) return;
    for (var i = this.firstAttached; i <= this.lastAttached; i++)
        this._hideBlock(i);


    this.firstAttached = null;
    this.lastAttached = null;
    this._adjustBlanks();
    //this.div.style.backgroundColor = "#eee";
};

Track.prototype.setLoaded = function() {
    this.loaded = true;
    this.hideAll();
    this.changed();
};

Track.prototype._loadingBlock = function(blockDiv) {
    blockDiv.appendChild(document.createTextNode("Loading..."));
    blockDiv.style.backgroundColor = "#eee";
    return 50;
};

Track.prototype._showBlock = function(blockIndex, startBase, endBase, scale) {
    if (this.blocks[blockIndex]) return this.blockHeights[blockIndex];
    if (this.empty) return this.labelHeight;

    var blockHeight;

    var blockDiv = document.createElement("div");
    blockDiv.className = "block";
    blockDiv.style.left = (blockIndex * this.widthPct) + "%";
    blockDiv.style.width = this.widthPct + "%";
    blockDiv.startBase = startBase;
    blockDiv.endBase = endBase;
    if (this.loaded) {
        blockHeight = this.fillBlock(blockDiv,
                                     this.blocks[blockIndex - 1],
                                     this.blocks[blockIndex + 1],
                                     startBase,
                                     endBase,
                                     scale,
                                     this.widthPx);
    } else {
        blockHeight = this._loadingBlock(blockDiv);
    }

    this.blocks[blockIndex] = blockDiv;
    this.blockHeights[blockIndex] = blockHeight;
    this.div.appendChild(blockDiv);
    return blockHeight;
};

Track.prototype.moveBlocks = function(delta) {
    var newBlocks = new Array(this.numBlocks);
    var newHeights = new Array(this.numBlocks);
    var i;
    for (i = 0; i < this.numBlocks; i++)
        newHeights[i] = 0;

    var destBlock;
    if ((this.lastAttached + delta < 0)
        || (this.firstAttached + delta >= this.numBlocks)) {
        this.firstAttached = null;
        this.lastAttached = null;
    } else {
        this.firstAttached = Math.max(0, Math.min(this.numBlocks - 1,
                                                 this.firstAttached + delta));
        this.lastAttached = Math.max(0, Math.min(this.numBlocks - 1,
                                                  this.lastAttached + delta));

        if (delta < 0)
            destBlock = newBlocks[this.firstAttached];
        else
            destBlock = newBlocks[this.lastAttached];
    }

    for (i = 0; i < this.numBlocks; i++) {
        var newIndex = i + delta;
        if ((newIndex < 0) || (newIndex >= this.numBlocks)) {
            //We're not keeping this block around, so delete
            //the old one.

            if (destBlock) this.transfer(this.blocks[i], destBlock);
            this._hideBlock(i);
        } else {
            //move block
            newBlocks[newIndex] = this.blocks[i];
            if (newBlocks[newIndex])
                newBlocks[newIndex].style.left =
                    ((newIndex) * this.widthPct) + "%";

            newHeights[newIndex] = this.blockHeights[i];
        }
    }
    this.blocks = newBlocks;
    this.blockHeights = newHeights;
    this._adjustBlanks();
};

Track.prototype.heightUpdate = function() {
    var maxHeight = 0;
    for (var i = this.firstAttached; i < this.lastAttached; i++)
        if (this.blockHeights[i] > maxHeight)
            maxHeight = this.blockHeights[i];
    return maxHeight;
};

Track.prototype.sizeInit = function(numBlocks, widthPct) {
    var i, oldLast;
    this.numBlocks = numBlocks;
    this.widthPct = widthPct;
    if (this.blocks && (this.blocks.length > 0)) {
        //if we're shrinking, clear out the end blocks
        for (i = numBlocks; i < this.blocks.length; i++)
            this._hideBlock(i);
        oldLast = this.blocks.length;
        this.blocks.length = numBlocks;
        this.blockHeights.length = numBlocks;
        //if we're expanding, set new blocks to be not there
        for (i = oldLast; i < numBlocks; i++) {
            this.blocks[i] = undefined;
            this.blockHeights[i] = 0;
        }
        this.lastAttached = Math.min(this.lastAttached, numBlocks - 1);
        if (this.firstAttached > this.lastAttached) {
            //not sure if this can happen
            this.firstAttached = null;
            this.lastAttached = null;
        }

        if (this.blocks.length != numBlocks) throw new Error("block number mismatch: should be " + numBlocks + "; blocks.length: " + this.blocks.length);
        for (i = 0; i < numBlocks; i++) {
            if (this.blocks[i]) {
                //if (!this.blocks[i].style) console.log(this.blocks);
                this.blocks[i].style.left = (i * widthPct) + "%";
                this.blocks[i].style.width = widthPct + "%";
            }
        }
    } else {
        this.initBlocks();
    }
};

/*

Copyright (c) 2007-2009 The Evolutionary Software Foundation

Created by Mitchell Skinner <mitch_skinner@berkeley.edu>

This package and its accompanying libraries are free software; you can
redistribute it and/or modify it under the terms of the LGPL (either
version 2.1, or at your option, any later version) or the Artistic
License 2.0.  Refer to LICENSE for the full license text.

*/
