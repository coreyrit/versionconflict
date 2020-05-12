
var fs = require('fs');
eval(fs.readFileSync('missioncontrol.js').toString());

function printGrid(grid) {
    for (var r = 0; r < grid.length; r++) {
        for (var c = 0; c < grid[r].length; c++) {
            if(grid[r][c] !== 0) {
                process.stdout.write(grid[r][c] + " ");
            } else {
                process.stdout.write("- ");
            }
        }
        console.log("")
    }
}

// console.log("______________________")
// printGrid(randomHoustonGeneration(9, 9, 5))
// console.log("______________________")
// printGrid(randomOrlandoGeneration(3, 3, 1))
// console.log("______________________")
// printGrid(randomOrlandoGeneration(4, 4, 2))
// console.log("______________________")
// printGrid(randomOrlandoGeneration(5, 5, 3))
// console.log("______________________")
// printGrid(randomOrlandoGeneration(6, 6, 4))
// console.log("______________________")

randomWashingtonGeneration("_+_=_", 1)
randomWashingtonGeneration("_-_=_", 1)
randomWashingtonGeneration("_*_=_", 1)
randomWashingtonGeneration("_+_+_=_+_", 2)
