window.onload = function scrollToInputError() {
    var elements = document.querySelectorAll(".ui-state-error");
    if (elements) {
        var first = elements[0];
        if (first) first.scrollIntoView();
    }
};