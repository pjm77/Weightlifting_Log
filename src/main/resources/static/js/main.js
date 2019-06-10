$(document).ready(function () {
    let topOfThePage = true;
    $(window).scroll(function () {
        if ($(this).scrollTop() > 0) {
            topOfThePage = false;
        } else {
            topOfThePage = true;
        }
    });
    $('html').bind('mousewheel DOMMouseScroll', function (e) {
        let delta = (e.originalEvent.wheelDelta || -e.originalEvent.detail);

        if (delta < 0) {
            console.log('Hide logo!');
            document.getElementById('logo-container').style.display="none";
        } else if (delta > 0 && topOfThePage === true) {
            console.log('Show logo!');
            document.getElementById('logo-container').style.display="block";
        }
    });
});

/* function updates a single lift section in General Strength tab
   whenever the user moves the slider it updates the corresponding number field and calculates 1 rep max
   when number field is updated it moves the slider and 1 rep max is calculated
   parameters are:
    fieldToUpdate - id of the corresponding field to update
    value - value that fieldToUpdate will be updated with
    weightField - id of the field holding weight for the current lift
    repsField - id of the field holding repetitions for the current lift
    maxField - id of the field holding the 1 rep max for the current lift that will be calculated */
function updateGS(fieldToUpdate, value, weightField, repsField, maxField) {
    document.getElementById(fieldToUpdate).value = value;
    let weight = document.getElementById(weightField).value;
    let reps = document.getElementById(repsField).value;
    document.getElementById(maxField).value = Math.round(weight * (1 + (reps / 30)) * 100) / 100;
}

/* function calculates the 1 rep max and percentages in 1 Rep Max tab */
function calculate1RM() {
    let weight = document.getElementById('weight-text').value;
    let reps = document.getElementById('reps-text').value;
    let result = weight * (1 + (reps / 30));
    document.getElementById('result').value = result;
    document.getElementById('percentage-1').innerText =
        (document.getElementById('percentage-input-1').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-2').innerText =
        (document.getElementById('percentage-input-2').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-3').innerText =
        (document.getElementById('percentage-input-3').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-4').innerText =
        (document.getElementById('percentage-input-4').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-5').innerText =
        (document.getElementById('percentage-input-5').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-6').innerText =
        (document.getElementById('percentage-input-6').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-7').innerText =
        (document.getElementById('percentage-input-7').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-8').innerText =
        (document.getElementById('percentage-input-8').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-9').innerText =
        (document.getElementById('percentage-input-9').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-10').innerText =
        (document.getElementById('percentage-input-10').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-11').innerText =
        (document.getElementById('percentage-input-11').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-12').innerText =
        (document.getElementById('percentage-input-12').value * 0.01 * result).toFixed(1);
    document.getElementById('percentage-13').innerText =
        (document.getElementById('percentage-input-13').value * 0.01 * result).toFixed(1);
}

/* This function updates an element of certain id with given value */
function update(field, val) {
    document.getElementById(field).value = val;
}


/* This function updates descriptions for percentages of 1 Rep Max */
function updatePercentageDescription(description, percentage) {
    let x = 0;
    let descriptions = [
        "0% - 20%",
        "21% - 40%",
        "41% - 60%",
        "61% - 80%",
        "81% - 90%",
        "91% - 100%"];
    if (percentage < 21) {
        x = 0;
    } else if (percentage < 41) {
        x = 1;
    } else if (percentage < 61) {
        x = 2;
    } else if (percentage < 81) {
        x = 3;
    } else if (percentage < 91) {
        x = 4;
    } else {
        x = 5;
    }
    document.getElementById(description).innerHTML = descriptions[x];
}