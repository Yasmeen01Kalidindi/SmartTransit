// ================= MST DATA =================

// Bus stops
const stops = [
    "SVECW",
    "Railway Station",
    "Town Hall",
    "Market",
    "Bhimavaram Bus Stand",
    "College Road"
];


// Network connections
const edges = [
    {
        from: "SVECW",
        to: "Railway Station",
        weight: 2.0
    },
    {
        from: "SVECW",
        to: "College Road",
        weight: 1.4
    },
    {
        from: "Railway Station",
        to: "Town Hall",
        weight: 1.2
    },
    {
        from: "College Road",
        to: "Town Hall",
        weight: 1.8
    },
    {
        from: "Town Hall",
        to: "Market",
        weight: 1.0
    },
    {
        from: "Market",
        to: "Bhimavaram Bus Stand",
        weight: 1.1
    },
    {
        from: "Town Hall",
        to: "Bhimavaram Bus Stand",
        weight: 2.0
    },
    {
        from: "Railway Station",
        to: "Market",
        weight: 1.7
    }
];


// ================= PRIM'S ALGORITHM =================

function primMST() {

    const visited = new Set();
    const mst = [];

    // Start from the first stop
    visited.add(stops[0]);

    while (visited.size < stops.length) {

        let smallestEdge = null;

        // Find the smallest edge connecting
        // a visited stop to an unvisited stop
        for (const edge of edges) {

            const fromVisited = visited.has(edge.from);
            const toVisited = visited.has(edge.to);

            if (fromVisited && !toVisited ||
                toVisited && !fromVisited) {

                if (
                    smallestEdge === null ||
                    edge.weight < smallestEdge.weight
                ) {
                    smallestEdge = edge;
                }
            }
        }

        // No connection found
        if (smallestEdge === null) {
            break;
        }

        mst.push(smallestEdge);

        // Add the new stop to visited
        visited.add(smallestEdge.from);
        visited.add(smallestEdge.to);
    }

    return mst;
}


// ================= KRUSKAL'S ALGORITHM =================

// Find the parent of a node
function findParent(parent, node) {

    if (parent[node] === node) {
        return node;
    }

    parent[node] = findParent(parent, parent[node]);

    return parent[node];
}


// Join two sets
function union(parent, rank, first, second) {

    const rootFirst = findParent(parent, first);
    const rootSecond = findParent(parent, second);

    if (rootFirst === rootSecond) {
        return false;
    }

    if (rank[rootFirst] < rank[rootSecond]) {

        parent[rootFirst] = rootSecond;

    } else if (rank[rootFirst] > rank[rootSecond]) {

        parent[rootSecond] = rootFirst;

    } else {

        parent[rootSecond] = rootFirst;
        rank[rootFirst]++;
    }

    return true;
}


// Kruskal algorithm
function kruskalMST() {

    const sortedEdges = [...edges].sort(
        (a, b) => a.weight - b.weight
    );

    const parent = {};
    const rank = {};

    // Initially every stop is its own set
    for (const stop of stops) {
        parent[stop] = stop;
        rank[stop] = 0;
    }

    const mst = [];

    for (const edge of sortedEdges) {

        const added = union(
            parent,
            rank,
            edge.from,
            edge.to
        );

        // Add edge only if it doesn't
        // create a cycle
        if (added) {

            mst.push(edge);

            if (mst.length === stops.length - 1) {
                break;
            }
        }
    }

    return mst;
}


// ================= DISPLAY RESULT =================

function displayMST(algorithmName, mstEdges) {

    const result = document.getElementById("mstResult");

    let totalDistance = 0;

    let edgeHTML = "";

    mstEdges.forEach(function (edge, index) {

        totalDistance += edge.weight;

        edgeHTML += `
            <div class="mst-edge">
                <strong>${index + 1}.</strong>
                ${edge.from}
                →
                ${edge.to}
                <span>${edge.weight.toFixed(1)} km</span>
            </div>
        `;
    });


    result.innerHTML = `
        <h2>🌳 ${algorithmName} Result</h2>

        <p>
            The algorithm selected
            <strong>${mstEdges.length}</strong>
            connections to connect all
            <strong>${stops.length}</strong>
            bus stops.
        </p>

        <div class="mst-result-summary">

            <strong>
                Minimum Total Distance:
            </strong>

            ${totalDistance.toFixed(1)} km

        </div>

        <div class="mst-edge-list">

            ${edgeHTML}

        </div>
    `;


    // Update the distance card
    document.getElementById("mstTotalDistance").textContent =
        totalDistance.toFixed(1) + " km";
}


// ================= BUTTON EVENTS =================

document.addEventListener("DOMContentLoaded", function () {

    const primButton =
        document.getElementById("primButton");

    const kruskalButton =
        document.getElementById("kruskalButton");


    // Prim button
    primButton.addEventListener("click", function () {

        const result = primMST();

        displayMST(
            "Prim's Algorithm",
            result
        );
    });


    // Kruskal button
    kruskalButton.addEventListener("click", function () {

        const result = kruskalMST();

        displayMST(
            "Kruskal's Algorithm",
            result
        );
    });

});