
function validateCount(grid, rows, columns, randomValues) {
    var count = 0;
    for (var r = 0; r < rows; r++) {
        for (var c = 0; c < columns; c++) {
            if(grid[r][c] !== 0) {
                count++;
            }
        }
    }
    if(count !== randomValues) {
        return false;
    }
    return true;
}

function validateSpacing(grid, rows, columns, space) {
    for(var r1 = 0; r1 < rows; r1++) {
        for(var c1 = 0; c1 < columns; c1++) {
            if(grid[r1][c1] !== 0) {
                for(var r2 = Math.max(0, r1-space); r2 < Math.min(r1+space, rows-1); r2++) {
                    for(var c2 = Math.max(0, c1-space); c2 < Math.min(c1+space, columns-1); c2++) {
                        if((r1 !== r2 || c1 !== c2) && grid[r2][c2] !== 0) {
                            return false;
                        }
                    }
                }
            }
        }
    }
    return true;
}


function validHoustonGrid(grid, rows, columns, randomValues) {
    // make sure there are the right number cells set
    if(!validateCount(grid, rows, columns, randomValues)) {
        return false;
    }

    // make sure all numbers are spread out
    if(!validateSpacing(grid, rows, columns, 2)) {
        return false;
    }

    // make sure no numbers are on the edges
    for(var r = 0; r < rows; r++) {
        if(grid[r][0] !== 0 || grid[r][columns-1] !== 0) {
            return false;
        }
    }
    for(var c = 0; c < columns; c++) {
        if(grid[0][c] !== 0 || grid[rows-1][c] !== 0) {
            return false;
        }
    }

    return true;
}

function validOrlandoGrid(grid, rows, columns, randomValues) {
    // make sure there are the right number cells set
    if(!validateCount(grid, rows, columns, randomValues)) {
        return false;
    }

    // make sure all numbers are spread out
    if(!validateSpacing(grid, rows, columns, 1)) {
        return false;
    }

    // make sure no column or row has the same number
    var maxLine = 1;
    for (var r = 0; r < rows; r++) {
        var map = {}; map[1] = 0; map[2] = 0; map[3] = 0; map[4] = 0; map[5] = 0; map[6] = 0;
        var count = 0;

        for (var c = 0; c < columns; c++) {
            if(grid[r][c] !== 0) {
                map[grid[r][c]]++;
                count++;
            }
        }
        if(count > maxLine) {
            return false;
        }
        for (var x = 1; x <= 6; x++) {
            if(map[x] > 1) {
                return false;
            }
        }
    }
    for (var c = 0; c < columns; c++) {
        var map = {}; map[1] = 0; map[2] = 0; map[3] = 0; map[4] = 0; map[5] = 0; map[6] = 0;
        var count = 0;

        for (var r = 0; r < rows; r++) {
            if(grid[r][c] !== 0) {
                map[grid[r][c]]++;
                count++;
            }
        }
        if(count > maxLine) {
            return false;
        }
        for (var x = 1; x <= 6; x++) {
            if(map[x] > 1) {
                return false;
            }
        }
    }

    return true;
}

function randomGridGeneration(rows, columns, randomValues) {
    var grid = [];
    for (var r = 0; r < rows; r++) {
        grid[r] = []
        for (var c = 0; c < columns; c++) {
            grid[r].push(0)
        }
    }

    for (var i = 0; i < randomValues; i++) {
        var r = Math.floor(Math.random() * rows)
        var c = Math.floor(Math.random() * columns)
        var x = Math.floor(Math.random() * 6) + 1
        grid[r][c] = x
    }
    return grid;
}

function randomHoustonGeneration(rows, columns, randomValues) {
    var grid = [];
    do {
        grid = randomGridGeneration(rows, columns, randomValues);
    } while(!validHoustonGrid(grid, rows, columns, randomValues));
    return grid
}

function randomOrlandoGeneration(rows, columns, randomValues) {
    var grid = [];
    do {
        grid = [];
        grid = randomGridGeneration(rows, columns, randomValues);
    } while(!validOrlandoGrid(grid, rows, columns, randomValues));
    return grid
}


function validateEquation(equation) {
    var parts = equation.split("=");
    // console.log(parts);

    // make sure both sides have at least 1 empty space
    // if(parts[0].indexOf("_") < 0) {
    //     return false;
    // }
    // if(parts[1].indexOf("_") < 0) {
    //     return false;
    // }


    var left1 = eval(parts[0].split("_").join("1"));
    var right1 = eval(parts[1].split("_").join("1"));

    var left6 = eval(parts[0].split("_").join("6"));
    var right6 = eval(parts[1].split("_").join("6"));

    // make sure its possible
    if(left6 < right1) {
        return false;
    }
    if(right6 < left1) {
        return false;
    }

    return true;
}

function randomWashingtonGeneration(equation, randomValues) {
    var s = equation;
    do {
        var s = equation;
        for (var i = 0; i < randomValues; i++) {
            var count = (s.match(/_/g) || []).length;
            var nth = 0;
            var target = Math.floor(Math.random() * count) + 1;
            var x = Math.floor(Math.random() * 6) + 1;
            s = s.replace(/_/g, function (match, i, original) {
                nth++;
                return (nth === target) ? x + "" : match;
            });
        }
    } while (!validateEquation(s));

    console.log(s);
}