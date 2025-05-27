async function getGlobalAlignment() {
    const seq1 = document.getElementById("seq1Global").value;
    const seq2 = document.getElementById("seq2Global").value;
    const gapPen = document.getElementById("gapPenGlobal").value;
    const mismatchPen = document.getElementById("mismatchPenGlobal").value;
    if(seq1 && seq2 && gapPen && mismatchPen){
        const response = await fetch(`/global-alignment?sequence1=${seq1}&sequence2=${seq2}&mismatch_penalty=${mismatchPen}&gap_penalty=${gapPen}`);
        const result = await response.json();
        document.getElementById("globalScore").innerText = "Score: " + result.score;
        document.getElementById("globalScore").style.color = "#000000";
        document.getElementById("globalCIGAR").innerText = "CIGAR String: " + result.cigar;

        //document.getElementById("showMatrix").style.display = "block";
    }
    else{
        document.getElementById("globalCIGAR").innerText = "";
        document.getElementById("globalScore").innerText = "Invalid information";
        document.getElementById("globalScore").style.color = "#ff0000";
        document.getElementById("showMatrix").style.display = "none";
    }
}

async function getFittingAlignment() {
    const seq1 = document.getElementById("seq1Fitting").value;
    const seq2 = document.getElementById("seq2Fitting").value;
    const gapPen = document.getElementById("gapPenFit").value;
    const mismatchPen = document.getElementById("mismatchPenFit").value;
    if(seq1 && seq2 && gapPen && mismatchPen){
        const response = await fetch(`/fitting-alignment?sequence1=${seq1}&sequence2=${seq2}&mismatch_penalty=${mismatchPen}&gap_penalty=${gapPen}`);
        const result = await response.json();
        document.getElementById("fitScore").innerText = "Score: " + result.score;
        document.getElementById("fitScore").style.color = "#000000";
        document.getElementById("fitRange").innerText = "The fitting range: " + result.y_start + " - " + (result.y_end - 1);
        document.getElementById("fitCIGAR").innerText = "CIGAR String: " + result.cigar;

        //document.getElementById("showMatrix").style.display = "block";
    }
    else{
        document.getElementById("fitScore").innerText = "Invalid information";
        document.getElementById("fitScore").style.color = "#ff0000";
        document.getElementById("fitRange").innerText = "";
        document.getElementById("fitCIGAR").innerText = "";
        document.getElementById("showMatrix").style.display = "none";
    }
}

async function buildFMIndex() {
    removeQueryFMInfo();
    const genome = document.getElementById("genome").value;
    if(genome){
        const response = await fetch(`/fm-index?genome=${genome}`);
        const result = await response.json();
        if(result.indexFCol != null || result.sa != null || result.tally != null || result.bwt != null){
            const fLabel = document.getElementById("f-index-label");
            fLabel.classList.remove("hidden", "error");
            fLabel.classList.add("visible-block", "valid");
            fLabel.innerText = "F-Index:";

            document.getElementById("f-index").classList.remove("hidden");
            document.getElementById("f-index").classList.add("visible-block");

            document.getElementById("$_count").innerText = result.indexFCol[0];
            document.getElementById("a_count").innerText = result.indexFCol[1];
            document.getElementById("c_count").innerText = result.indexFCol[2];
            document.getElementById("g_count").innerText = result.indexFCol[3];
            document.getElementById("t_count").innerText = result.indexFCol[4];

            document.getElementById("sa").classList.remove("hidden");
            document.getElementById("sa").classList.add("visible-block");
            document.getElementById("sa").innerText = "Suffix Array: " + result.sa;
            
            document.getElementById("bwt").classList.remove("hidden");
            document.getElementById("bwt").classList.add("visible-block");
            document.getElementById("bwt").innerText = "BWT: " + result.bwt;
            
            document.getElementById("query").classList.remove("hidden");
            document.getElementById("query").classList.add("visible-inline");
            //document.getElementById("partialQuery").classList.remove("hidden");
            //document.getElementById("partialQuery").classList.add("visible-inline");
            document.getElementById("completeQuery").classList.remove("hidden");
            document.getElementById("completeQuery").classList.add("visible-inline");
        }
        else{
            removeBuildFMInfo();
            invalidBuildGenome();
        }
    }
    else{
        removeBuildFMInfo();
        invalidBuildGenome();
    }
}

function invalidBuildGenome(){
    const fLabel = document.getElementById("f-index-label");
    fLabel.classList.remove("hidden", "valid");
    fLabel.classList.add("visible-block", "error");
    fLabel.innerText = "Invalid Genome/Sequence (Make sure the Sentinel '$' is at the end of the Genome)";
}

function removeBuildFMInfo(){
    document.getElementById("f-index-label").classList.add("hidden");
    document.getElementById("f-index-label").classList.remove("visible-block", "error", "valid");
    document.getElementById("f-index").classList.add("hidden");
    document.getElementById("f-index").classList.remove("visible-block");
    document.getElementById("query").classList.add("hidden");
    document.getElementById("query").classList.remove("visible-inline");
    //document.getElementById("partialQuery").classList.add("hidden");
    //document.getElementById("partialQuery").classList.remove("visible-inline");
    document.getElementById("completeQuery").classList.add("hidden");
    document.getElementById("completeQuery").classList.remove("visible-inline");
    document.getElementById("sa").classList.add("hidden");
    document.getElementById("sa").classList.remove("visible-block");
    document.getElementById("bwt").classList.add("hidden");
    document.getElementById("bwt").classList.remove("visible-block");
}

function removeQueryFMInfo(){
    document.getElementById("num-of-matches").classList.add("hidden");
    document.getElementById("match-list").classList.add("hidden");
    document.getElementById("num-of-matches").classList.remove("visible-block", "error", "valid");
    document.getElementById("match-list").classList.remove("visible-block");
}

async function completeQuery() {
    const numMatches = document.getElementById("num-of-matches");
    const matchList = document.getElementById("match-list");
    const queryInput = document.getElementById("query");

    const query = queryInput.value;
    if(query){
        if(validGenome(query)){
            const response = await fetch(`/complete-query?query=${query}`);
            const result = await response.json();
            numMatches.classList.remove("hidden", "error");
            numMatches.classList.add("visible-block", "valid");
            matchList.classList.remove("hidden");
            matchList.classList.add("visible-block");

            numMatches.innerText = "Number of Matches: " + result.occs;
            matchList.innerText = "Matches found at: " + result.occ_list;
        }
        else{
            numMatches.classList.remove("hidden", "valid");
            numMatches.classList.add("visible-block", "error");
            numMatches.innerText = "Invalid Genome (Do NOT include the Sentinel '$')";
            matchList.classList.add("hidden");
            matchList.classList.remove("visible-block");
        }
    }
    else{
        numMatches.classList.remove("hidden", "valid");
        numMatches.classList.add("visible-block", "error");
        numMatches.innerText = "Invalid information";
        matchList.classList.add("hidden");
        matchList.classList.remove("visible-block");
    }
}


function validGenome(str) {
    for (let i = 0; i < str.length; i++) {
        if (str[i] !== "A" && str[i] !== "C" && str[i] !== "G" && str[i] !== "T") {
            return false;
        }
    }
    return true;
}

function preloadFittingData() {
    document.getElementById("seq1Fitting").value = "GACC";
    document.getElementById("seq2Fitting").value = "TAGACCATT";
    document.getElementById("mismatchPenFit").value = 1;
    document.getElementById("gapPenFit").value = 2;
}
