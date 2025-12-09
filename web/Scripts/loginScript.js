/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


 function abrirModal() {
                document.getElementById("modalRecuperar").style.display = "block";
            }

function cerrarModal() {
                document.getElementById("modalRecuperar").style.display = "none";
            }

window.onclick = function(e) {
                if (e.target === document.getElementById("modalRecuperar")) {
                    
        
        cerrarModal();
                }
            };