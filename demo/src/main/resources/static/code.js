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
        document.getElementById("fitRange").innerText = "The fitting range: " + result.y_start + " - " + result.y_end;
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
            document.getElementById("f-index-label").style.display = "block";
            document.getElementById("f-index-label").innerText = "F-Index:";
            document.getElementById("f-index-label").style.color = "#000000";
            document.getElementById("f-index").style.display = "block";


            document.getElementById("$_count").innerText = result.indexFCol[0];
            document.getElementById("a_count").innerText = result.indexFCol[1];
            document.getElementById("c_count").innerText = result.indexFCol[2];
            document.getElementById("g_count").innerText = result.indexFCol[3];
            document.getElementById("t_count").innerText = result.indexFCol[4];

            document.getElementById("sa").style.display = "block";
            document.getElementById("sa-link").style.display = "inline";
            document.getElementById("sa-credit").style.display = "inline";
            document.getElementById("sa").innerText = "Suffix Array: " + result.sa;
            
            
            document.getElementById("bwt").style.display = "block";
            document.getElementById("bwt").innerText = "BWT: " + result.bwt;
            
            
            document.getElementById("query").style.display = "inline";
            //document.getElementById("partialQuery").style.display = "inline";
            document.getElementById("completeQuery").style.display = "inline";
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
    document.getElementById("f-index-label").style.display = "block";
    document.getElementById("f-index-label").style.color = "#ff0000";
    document.getElementById("f-index-label").innerText = "Invalid Genome/Sequence (Make sure the Sentinel \'$\' is at the end of the Genome)";
}

function removeBuildFMInfo(){
    document.getElementById("f-index-label").style.display = "none";
    document.getElementById("f-index").style.display = "none";
    document.getElementById("query").style.display = "none";
    //document.getElementById("partialQuery").style.display = "none";
    document.getElementById("completeQuery").style.display = "none";
    document.getElementById("sa").style.display = "none";
    document.getElementById("sa-link").style.display = "none";
    document.getElementById("sa-credit").style.display = "none";
    document.getElementById("bwt").style.display = "none";
}

function removeQueryFMInfo(){
    document.getElementById("num-of-matches").style.display = "none";
    document.getElementById("match-list").style.display = "none";
}

async function completeQuery() {


    const query = document.getElementById("query").value;
    if(query){
        if(validGenome(query)){
            const response = await fetch(`/complete-query?query=${query}`);
            const result = await response.json();
            document.getElementById("num-of-matches").style.display = "block";
            document.getElementById("num-of-matches").style.color = "#000000";
            document.getElementById("match-list").style.display = "block";

            document.getElementById("num-of-matches").innerText = "Number of Matches: " + result.occs;
            document.getElementById("match-list").innerText = "Matches found at: " + result.occ_list;
        }
        else{
            document.getElementById("num-of-matches").style.display = "block";
            document.getElementById("num-of-matches").style.color = "#ff0000";
            document.getElementById("num-of-matches").innerText = "Invalid Genome (Do NOT include the Sentinel '$')";
            document.getElementById("match-list").style.display = "none";
        }
    }
    else{
        document.getElementById("num-of-matches").style.display = "block";
        document.getElementById("num-of-matches").style.color = "#ff0000";
        document.getElementById("num-of-matches").innerText = "Invalid information";
        document.getElementById("match-list").style.display = "none";
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