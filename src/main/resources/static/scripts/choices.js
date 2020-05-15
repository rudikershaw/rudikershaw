(() => {
    document.querySelectorAll('.choices li').forEach(button => {
        const choices = button.closest('.choices').querySelectorAll('.choice');
        const siblingButtons = button.parentNode.querySelectorAll('li');

        button.onclick = () => {
            siblingButtons.forEach(li => li.classList.remove('selected'));
            button.classList.add('selected');

            choices.forEach(choice => {
                if (choice.getAttribute('choice') === button.textContent) {
                    choice.style.display = null;
                } else {
                    choice.style.display = 'none';
                }
            });
        };
    });
})();
