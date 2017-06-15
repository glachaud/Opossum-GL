/* VARIABLES GLOBALES */

var timer_achievement;
var liste_emojis = "<i class='em em---1'></i><i class='em em--1'></i><i class='em em-expressionless'></i><i class='em em-angry'></i><i class='em em-anguished'></i><i class='em em-astonished'></i><i class='em em-blush'></i><i class='em em-bowtie'></i><i class='em em-cold_sweat'></i><i class='em em-confounded'></i><i class='em em-confused'></i><i class='em em-cry'></i><i class='em em-disappointed'></i><i class='em em-disappointed_relieved'></i> <i class='em em-dizzy_face'></i><i class='em em-fearful'></i><i class='em em-flushed'></i><i class='em em-frowning'></i><i class='em em-grimacing'></i><i class='em em-grinning'></i><i class='em em-heart_eyes'></i><i class='em em-hushed'></i><i class='em em-innocent'></i><i class='em em-joy'></i><i class='em em-laughing'></i><i class='em em-neutral_face'></i><i class='em em-no_mouth'></i><i class='em em-open_mouth'></i><i class='em em-pensive'></i><i class='em em-persevere'></i><i class='em em-rage'></i><i class='em em-relaxed'></i><i class='em em-relieved'></i><i class='em em-satisfied'></i><i class='em em-scream'></i><i class='em em-sleeping'></i><i class='em em-sleepy'></i><i class='em em-smile'></i><i class='em em-smiley'></i><i class='em em-smirk'></i><i class='em em-sob'></i><i class='em em-stuck_out_tongue'></i><i class='em em-stuck_out_tongue_closed_eyes'></i><i class='em em-sunglasses'></i><i class='em em-sweat'></i><i class='em em-sweat_smile'></i><i class='em em-tired_face'></i><i class='em em-triumph'></i><i class='em em-unamused'></i><i class='em em-weary'></i><i class='em em-wink'></i><i class='em em-worried'></i><i class='em em-yum'></i><i class='em em-alarm_clock'></i><i class='em em-alien'></i><i class='em em-ambulance'></i><i class='em em-arrow_left'></i><i class='em em-arrow_lower_left'></i><i class='em em-arrow_right'></i><i class='em em-arrow_lower_right'></i><i class='em em-arrow_upper_left'></i><i class='em em-arrow_upper_right'></i><i class='em em-arrow_up'></i><i class='em em-arrow_up_down'></i><i class='em em-arrows_counterclockwise'></i><i class='em em-art'></i><i class='em em-baby_bottle'></i><i class='em em-baby'></i><i class='em em-bar_chart'></i><i class='em em-basketball'></i><i class='em em-beer'></i><i class='em em-bell'></i><i class='em em-bike'></i><i class='em em-birthday'></i><i class='em em-blue_car'></i><i class='em em-boat'></i><i class='em em-book'></i><i class='em em-books'></i><i class='em em-boom'></i><i class='em em-briefcase'></i><i class='em em-bulb'></i><i class='em em-broken_heart'></i><i class='em em-heart'></i><i class='em em-bus'></i><i class='em em-bullettrain_front'></i><i class='em em-bust_in_silhouette'></i><i class='em em-calendar'></i><i class='em em-camera'></i><i class='em em-checkered_flag'></i><i class='em em-clap'></i><i class='em em-clapper'></i><i class='em em-clipboard'></i><i class='em em-clock1030'></i><i class='em em-closed_lock_with_key'></i><i class='em em-cloud'></i><i class='em em-coffee'></i><i class='em em-computer'></i><i class='em em-construction'></i><i class='em em-cookie'></i><i class='em em-dart'></i><i class='em em-dash'></i><i class='em em-donut'></i><i class='em em-earth_africa'></i><i class='em em-egg'></i><i class='em em-envelope'></i><i class='em em-euro'></i><i class='em em-eyes'></i><i class='em em-eyeglasses'></i><i class='em em-facepunch'></i><i class='em em-fast_forward'></i><i class='em em-fire'></i><i class='em em-file_folder'></i><i class='em em-fist'></i><i class='em em-gift'></i><i class='em em-hankey'></i><i class='em em-mag'></i><i class='em em-mailbox'></i><i class='em em-mega'></i><i class='em em-no_bell'></i><i class='em em-notes'></i><i class='em em-ok_hand'></i><i class='em em-pushpin'></i><i class='em em-snail'></i><i class='em em-squirrel'></i><i class='em em-star'></i><i class='em em-speech_balloon'></i><i class='em em-trophy'></i><i class='em em-tv'></i><i class='em em-hamburger'></i><i class='em em-zzz'></i><i class='em em-zap'></i><i class='em em-uk'></i><i class='em em-us'></i><i class='em em-kr'></i><i class='em em-jp'></i><i class='em em-it'></i><i class='em em-fr'></i><i class='em em-es'></i><i class='em em-de'></i><i class='em em-cn'></i>";
var chartsLive = [];

$( document ).ready(function(){

	console.log("Pensez-vous que le snake mange des Opossums ? Il doit y avoir un moyen de vérifier...");
	//Quelque chose comme un jeu... Un jeu snake, ça pourrait nous permettre de vérifier ça !

	moment.locale('fr');

	/*
	 PARTIE "GLOBALE" (toujours activé car utile à une très grande majorité des pages...)
	 */

	//On met les temps via moment.js
	$("*[data-time]").each(function(){
		$(this).text(moment($(this).attr("data-time")).fromNow());
	});
	
	//On vire la colonne de gauche au clique sur le côté quand on est sur tablette / mobile
	$("body").on("click", ".colonne-droite", function(){
		if($(window).width() < 993){
			var className = $("#liste-responsive").find(".glyphicon").attr("class");
			if(className == "glyphicon glyphicon glyphicon-backward"){
				$( "#colonne-gauche" ).toggle( "slide" );
				$( ".card" ).toggle("slide", function(){
					if ( $('.panel_scroll_profil').length ){
						$(".panel_scroll_profil").outerHeight($( window ).height()-$('.navtabs_profil').offset().top-$('.navtabs_profil').height()+10);
					}
				});
				$("#liste-responsive").find(".glyphicon").attr("class", "glyphicon glyphicon glyphicon-forward");
			}
		}
	});

	//Fixe un probleme avec les popover qui ont un z-idnex trop grand et bloque les input
	$('body').on('hidden.bs.popover', function() {
		$('.popover:not(.in)').hide().detach();
	});

	//Gestion du changement des emojis en fonction de la position du slider
	$('body').on('change', ".input_emoji", function () {
		//Récupération d'infos diverses utiles...
		var question = $(this).attr("data-question")
		var val = $(this).val();
		var num = $(this).attr("data-question");
		var className = "popover-emoji em ";
		var parent = $(this).parents(".panel");
		if($(this).parents(".modal").length){
			parent = $(this).parents(".modal");
		}
		//Récupération des emojis sauvegardés pour ce slider via les input hidden
		var tres_mauvais = parent.find("input[name=tres_mauvais_"+num+"]").val();
		var mauvais = parent.find("input[name=mauvais_"+num+"]").val();
		var mitige = parent.find("input[name=mitige_"+num+"]").val();
		var heureux = parent.find("input[name=heureux_"+num+"]").val();
		var tres_heureux = parent.find("input[name=tres_heureux_"+num+"]").val();
		//En fonction de la valeur du slider, affichage du bon emoji
		if(val < 20){
			className += tres_mauvais;
		}else if(val < 40){
			className += mauvais;
		}else if(val < 60){
			className += mitige;
		}else if(val < 80){
			className += heureux;
		}else{
			className += tres_heureux;
		}
        parent.find("#emoji-"+question).attr("class", className);
	});

	//Set des popovers Bootstrap
	activatePopovers();

	//Set des tooltips Bootstrap
	$('[data-toggle="tooltip"]').tooltip();

	//Bootstrap, pour les modals
	$(".modal").appendTo("body");

	//Pour le header : on retire la colonne de gauche et on change l'icone du header en fonction...
	$( "#liste-responsive" ).click(function() {
		$( "#colonne-gauche" ).toggle( "slide" );
		$( ".card" ).toggle("slide", function(){
			if ( $('.panel_scroll_profil').length ){
				$(".panel_scroll_profil").outerHeight($( window ).height()-$('.navtabs_profil').offset().top-$('.navtabs_profil').height()+10);
			}
		});
		var className = $(this).find(".glyphicon").attr("class");
		if(className == "glyphicon glyphicon glyphicon-backward"){
			$(this).find(".glyphicon").attr("class", "glyphicon glyphicon glyphicon-forward");
		}else{
			$(this).find(".glyphicon").attr("class", "glyphicon glyphicon glyphicon-backward");
		}
	});

	//Set de la height du div qui permettra de scroll en fonction de ce qu'il y a sur la page.
	if ( $('#first_row_home').length ){
		$(".scrollableDiv").outerHeight((window.innerHeight-$(".header").outerHeight()-$('#first_row_home').outerHeight()));
	}else{
		$(".scrollableDiv").outerHeight((window.innerHeight-$(".header").outerHeight()));
	}
	$( window ).resize(function() {
		if ( $('#first_row_home').length ){
			$(".scrollableDiv").outerHeight((window.innerHeight-$(".header").outerHeight()-$('#first_row_home').outerHeight()));
		}else{
			$(".scrollableDiv").outerHeight((window.innerHeight-$(".header").outerHeight()));
		}
	});

	//Affichage d'un succès
	$(".achievement").click(function(){
		$( ".achievement" ).toggle( "clip" );
		clearTimeout(timer_achievement);
	});

	//Set des datepickers
	//$.datepicker.setDefaults( $.datepicker.regional[ "fr" ] );
	$(".datepick").datetimepicker({locale: 'fr'});

	//Qu'on soit directement sur le premier input quand un pop-up s'ouvre (agréable pour les mobile-users)
	$(".modal").on('shown.bs.modal', function () {
		$(this).find("input:visible:first").focus();
	});

	/*
	PARTIE COLONNE PROFESSEUR (AJOUT QUESTIONNAIRE, AJOUT / MODIFICATION / SUPPRESSION D'UN DOSSIER
	 */

	//Autocomplete module eleve delete
	if($("#student_to_del").length){
		autocomplete();
    }


	//Quand on voit ces notifications, les commentaires sont bien mis en "lu" à présent
	$("#notifications_prof").click(function(){
		$.post("/commentaire/lu", {}, function(data){});
		$(this).find(".badge").remove();
	});

	//Utile pour "reset" le pop-up d'ajout d'un questionnaire.
	var modal_template = $("#form_ajout_questionnaire").html();

	//DatePicker activé / desactivé
	$('body').on("click", "#enable_datepicker, #enable_datepicker2", function(){
		if(this.checked){
			$('.datepick').prop("disabled", false);
		}else{
			$('.datepick').prop("disabled", true);
		}
	});

	//Affichage des inputs en fonction du type de question selectionné
	$('body').on('click', ".type_question", function () {
        var id = "all_questions";
        if($(this).parents('#modification').length){
            id= "all_questions2";
        }
		var question = $(this).attr("data-question");
		var type = $(this).attr("data-type");
		if($("#"+id).find("#type_question_"+question).val() != ""){
			$("#"+id).find("#"+$("#"+id).find("#type_question_"+question).val()+"_"+question).hide();
		}
        $("#"+id).find("#type_question_"+question).val(type);
        $("#"+id).find("#"+type+"_"+question).fadeIn();
	});

	//On enresgistre l'id de la question pour laquelle il faut changer l'emoji
	$('body').on('click', ".popover-emoji", function(){
		var modal;
		if($(this).parents("#modification").length){
			modal = "modification";
		}else{
			modal = "form_ajout_questionnaire";
		}
		var id = $(this).attr("data-id");
		$("#"+modal).find("#emoji_change").val(id);
	});

	//Change le smiley pour un des intervalles de valeurs du slider emoji quand on clique sur un nouvel emoji
	//Du sélecteur d'emojis...
	$('body').on('click', ".popover i", function () {
        var modal;
        if($(this).parents("#modification").length){
            modal = "modification";
        }else if($(this).parents("#form_ajout_questionnaire").length){
            modal = "form_ajout_questionnaire";
        }else{
        	modal = "add_el_div";
		}
		//On vire le "em " inutile
		var emoji = $(this).attr("class").substring(3);
		//On ferme le popover emoji
		$(this).closest(".popover").popover("hide");
		//On regarde pour quel intervalle de valeur du slider l'emoji doit changer, puis on le change dans l'input hidden
		var val = $(this).closest(".popover").prev().find(".input_emoji").val();
		var question = $("#"+modal).find("#emoji_change").val();
		if($(".btn_live_off").length){
			question = 1;	//Cas du live ou il n'y a qu'une question
		}
		if(val < 20){
            $("#"+modal).find("input[name='tres_mauvais_"+question+"']").attr("value", emoji);
		}else if(val < 40){
            $("#"+modal).find("input[name='mauvais_"+question+"']").attr("value", emoji);
		}else if(val < 60){
            $("#"+modal).find("input[name='mitige_"+question+"']").attr("value", emoji);
		}else if(val < 80){
            $("#"+modal).find("input[name='heureux_"+question+"']").attr("value", emoji);
		}else{
            $("#"+modal).find("input[name='tres_heureux_"+question+"']").attr("value", emoji);
		}
        $("#"+modal).find("#emoji-"+question).attr("class", "em "+emoji);
	});

	/* Gestion de l'édition du questionnaire */
	$("body").on("click", "#edit_questionnaire_submit", function(){
        //Reset si y'avait des erreurs avant.
        resetErrors();
        //Gestion des erreurs, avant ajout si tout est OK.
        var error = false;
        //Check du titre
        id_modal = "form_ajout_questionnaire";
        if($("#modification").length && $(this).parents("#modification").length){
            id_modal = "modification"
        }
        if($('#enable_datepicker2').is(":checked")){
            var groupeDate = $("#"+id_modal).find(".date");
            groupeDate.find("input").each(function(){
                if($(this).val() == ""){
                    groupeDate.addClass("has-error");
                    error =true;
                }
            });
        }
        var template = $("#"+id_modal).find("#template").is(":checked");
        var id_questionnaire = parseInt($(this).attr("data-questionnaire"));
        var title = $("#"+id_modal).find("#titre_questionnaire").val();
        if(title == ""){
            $("#"+id_modal).find("#titre_questionnaire").parent().addClass("has-error");
            $("#"+id_modal).find("#titre_questionnaire").parent().find("label").addClass("text-red");
            error = true;
        }
        // Check si on a toujours un titre pour les questions + si elle a un type de choisie
        // + au moins un selecteur + notation > 0 et notation1 < notation2
        $("#"+id_modal).find(".div_question").each(function(){
            var id = $(this).attr("data-id");
            if($(this).find("#titre_question").val() == ""){
                $(this).find("#titre_question").parent().addClass("has-error");
                $(this).find("#titre_question").parent().prev().find('label').addClass("text-red");
                error = true;
            }
            if($(this).find("input[name='type_question']").val() == ""){
                $(this).find("input[name='type_question']").parent().prev().find('label').addClass("text-red");
                error = true;
            }
            if($(this).find("input[name='type_question']").val() == "selecteur"){
                $("#groupe_select-"+id).find("input").each(function(){
                    if($(this).val() == "") {
                        $(this).parent().addClass("has-error");
                        $("#selecteur_"+id).find(".label_question_modal").addClass("text-red");
                        error = true;
                    }
                });
            }
            if($(this).find("input[name='type_question']").val() == "qcm"){
				/*var auMoinsUnJuste = false; */
                $("#groupe_qcm-"+id).find("input").each(function(){
                    if($(this).val() == "") {
                        $(this).parent().addClass("has-error");
                        $("#qcm_"+id).find(".label_question_modal").addClass("text-red");
                        error = true;
                    }/*if($(this).attr("class") == "form-control is_true"){
					 auMoinsUnJuste = true;
					 }*/
                });
				/*if(!auMoinsUnJuste){
				 $("#groupe_qcm-"+id).addClass("has-error");
				 $("#qcm_"+id).find(".label_question_modal").addClass("text-red");
				 error = true;
				 }*/
            }
            if($(this).find("input[name='type_question']").val() == "notation"){
                var val1 = $("#notation_"+id).find("input[name='valeur_question']").val();
                var val2 = $("#notation_"+id).find("input[name='valeur_question2']").val();
                if(val1 == "" || val2 == "" || parseInt(val1) < 0 || parseInt(val2) < 0 || parseInt(val1) > parseInt(val2)){
                    $("#notation_"+id).find("input[name='valeur_question']").parent().addClass("has-error");
                    $("#notation_"+id).find("input[name='valeur_question2']").parent().addClass("has-error");
                    $("#notation_"+id).find(".label_question_modal").addClass("text-red");
                    error = true;
                }
            }
        });
        var nb_question = $("#"+id_modal).find(".div_question").length;
		if(!error){
            var dateDebut = "";
            var dateFin = "";
            if($("#"+id_modal).find('#enable_datepicker2').is(":checked")){
                dateDebut = $("#"+id_modal).find(".date").find("input").eq(0).val();
                dateFin = $("#"+id_modal).find(".date").find("input").eq(1).val();
            }
			$.post("/questionnaire/edit", {id: id_questionnaire, titre: title,
				nb_question: nb_question, dateDebut: dateDebut, dateFin: dateFin, anonymat: $("#anonymat2").is(":checked")},
				function(data){
                    var numero = 0;
                    if(data != "nok") {
                        var questions = [];
                        //On les parcourt toutes
						$("#"+id_modal).find(".div_question").each(function () {
							var index = $(this).attr("data-id");
                            var id_question = $(this).attr("data-question");
                            var question = $(this).find("#titre_question").val();
                            var type = $(this).find("#type_question_" + index).val();
                            var emojis = "";
                            var select = "";
                            var right_answers = "";
                            var qcm = "";
                            var min_range = "";
                            var max_range = "";
                            if (type == "selecteur") {
                                $("#"+id_modal).find("#groupe_select-" + index).find("input").each(function () {
                                    select += ($(this).val()) + ';';
                                });
                                select.slice(0, -1);
                            } else if (type == "slider") {
                                emojis = $("#" + id_modal).find("input[name='tres_mauvais_" + index + "']").val() + "," + $("#" + id_modal).find("input[name=mauvais_" + index + "]").val() + "," + $("#" + id_modal).find("input[name='mitige_" + index + "']").val() + "," + $("#" + id_modal).find("input[name='heureux_" + index + "']").val() + "," + $("#" + id_modal).find("input[name='tres_heureux_" + index + "']").val();
                            } else if (type == "notation") {
                                min_range = $("#" + id_modal).find("#notation_" + index).find('input[name="valeur_question"]').val();
                                max_range = $("#" + id_modal).find("#notation_" + index).find('input[name="valeur_question2"]').val();
                            } else if (type == "qcm") {
                                $("#" + id_modal).find("#groupe_qcm-" + index).find("input").each(function () {
                                    qcm += ($(this).val()) + ';';
                                    if ($(this).attr("class") == "form-control is_true") {
                                        right_answers += ($(this).val()) + ';';
                                    }
                                });
                                qcm.slice(0, -1);
                                right_answers.slice(0, -1);
                            }
                            questions.push({id: id_question, question: question, id_questionnaire: id_questionnaire,
								type: type, emojis: emojis, select:select, qcm: qcm, right_answers: right_answers,
								max_range: parseInt(max_range), min_range: parseInt(min_range), index: parseInt(index)});
                    });
					var json = JSON.stringify(questions);
					$.post("/question/edit", {datas: json, id_questionnaire: id_questionnaire}, function(data){
						if(data == "nok"){
                            $.notify("Une erreur est survenu pour l'édition des questions", "warn");
                        }
					});
				}else{
					$.notify("Une erreur est survenu pour l'édition du questionnaire", "warn");
				}
			});
            //Ajout template si selectionné
			var nom_template = $("#"+id_modal).find(".template_name").find("input").val();
			$.post("/template/edit", {nom: nom_template, id_questionnaire: id_questionnaire, checked: $("#template2").is(":checked")}, function(data){
				if(data == "nok"){
                    $.notify("Une erreur est survenu pour l'édition du template", "warn");
                }else{
                    location.reload();
                }
			});
		}
    });

	/* Gestion de la création d'un nouveau questionnaire */
	$("body").on("click", "#questionnaire_submit", function(){
		//Reset si y'avait des erreurs avant.
		resetErrors();
		//Gestion des erreurs, avant ajout si tout est OK.
		var error = false;
		//Check du titre
		id_modal = "form_ajout_questionnaire";
		if($("#modification").length && $(this).parents("#modification").length){
			id_modal = "modification"
		}
		var title = $("#"+id_modal).find("#titre_questionnaire").val();
		var template = $("#"+id_modal).find("#template").is(":checked");
		if(title == ""){
            $("#"+id_modal).find("#titre_questionnaire").parent().addClass("has-error");
            $("#"+id_modal).find("#titre_questionnaire").parent().find("label").addClass("text-red");
			error = true;
		}
		if($('#enable_datepicker').is(":checked")){
			var groupeDate = $("#"+id_modal).find(".date");
			groupeDate.find("input").each(function(){
				if($(this).val() == ""){
					groupeDate.addClass("has-error");
				}
			});
		}
		// Check si on a toujours un titre pour les questions + si elle a un type de choisie
		// + au moins un selecteur + notation > 0 et notation1 < notation2
        $("#"+id_modal).find(".div_question").each(function(){
			var id = $(this).attr("data-id");
			if($(this).find("#titre_question").val() == ""){
				$(this).find("#titre_question").parent().addClass("has-error");
				$(this).find("#titre_question").parent().prev().find('label').addClass("text-red");
				error = true;
			}
			if($(this).find("input[name='type_question']").val() == ""){
				$(this).find("input[name='type_question']").parent().prev().find('label').addClass("text-red");
				error = true;
			}
			if($(this).find("input[name='type_question']").val() == "selecteur"){
				$("#groupe_select-"+id).find("input").each(function(){
					if($(this).val() == "") {
						$(this).parent().addClass("has-error");
						$("#selecteur_"+id).find(".label_question_modal").addClass("text-red");
						error = true;
					}
				});
			}
			if($(this).find("input[name='type_question']").val() == "qcm"){
				/*var auMoinsUnJuste = false; */
				$("#groupe_qcm-"+id).find("input").each(function(){
					if($(this).val() == "") {
						$(this).parent().addClass("has-error");
						$("#qcm_"+id).find(".label_question_modal").addClass("text-red");
						error = true;
					}/*if($(this).attr("class") == "form-control is_true"){
						auMoinsUnJuste = true;
					}*/
				});
				/*if(!auMoinsUnJuste){
					$("#groupe_qcm-"+id).addClass("has-error");
					$("#qcm_"+id).find(".label_question_modal").addClass("text-red");
					error = true;
				}*/
			}
			if($(this).find("input[name='type_question']").val() == "notation"){
				var val1 = $("#notation_"+id).find("input[name='valeur_question']").val();
				var val2 = $("#notation_"+id).find("input[name='valeur_question2']").val();
				if(val1 == "" || val2 == "" || parseInt(val1) < 0 || parseInt(val2) < 0 || parseInt(val1) > parseInt(val2)){
					$("#notation_"+id).find("input[name='valeur_question']").parent().addClass("has-error");
					$("#notation_"+id).find("input[name='valeur_question2']").parent().addClass("has-error");
					$("#notation_"+id).find(".label_question_modal").addClass("text-red");
					error = true;
				}
			}
		});
		if(template && $(".template_name").find("input").val() == ""){
			error = true;
			$(".template_name").addClass("has-error");
		}
		var nb_question = $("#"+id_modal).find(".div_question").length;
		//Si pas d'erreur, ajout du questionnaire et des questions
		if(error == false) {
			var dateDebut = "";
			var dateFin = "";
			if($("#"+id_modal).find('#enable_datepicker').is(":checked")){
				dateDebut = $("#"+id_modal).find(".date").find("input").eq(0).val();
				dateFin = $("#"+id_modal).find(".date").find("input").eq(1).val();
			}
			$.post("/questionnaire/add_questionnaire", {
				titre_questionnaire: title,
				nb_question: nb_question,
				module_id: parseInt($("#module_id").val()),
				dateDebut: dateDebut,
				dateFin: dateFin,
				anonymat: $("#"+id_modal).find("#anonymat").is(":checked")
			}, function (data) {
				//Si ajout sans problème, on passe au question du questionnaire
				if (data != "-1") {
					var index = 0;
					//On les parcourt toutes
                    $("#"+id_modal).find(".div_question").each(function () {
						index += 1;
						var id_question = $(this).attr("data-id");
						var question = $(this).find("#titre_question").val();
						var type = $(this).find("#type_question_" + id_question).val();
						var emojis = "";
						var select = "";
						var right_answers = "";
						var qcm = "";
						var min_range = "";
						var max_range = "";
						if (type == "selecteur") {
                            $("#" + id_modal).find("#groupe_select-" + id_question).find("input").each(function () {
								select += ($(this).val()) + ';';
							});
							select.slice(0, -1);
						} else if (type == "slider") {
                            emojis = $("#" + id_modal).find("input[name='tres_mauvais_" + index + "']").val() + "," + $("#" + id_modal).find("input[name=mauvais_" + index + "]").val() + "," + $("#" + id_modal).find("input[name='mitige_" + index + "']").val() + "," + $("#" + id_modal).find("input[name='heureux_" + index + "']").val() + "," + $("#" + id_modal).find("input[name='tres_heureux_" + index + "']").val();
						} else if (type == "notation") {
							min_range = $("#"+id_modal).find("#notation_" + id_question).find('input[name="valeur_question"]').val();
							max_range = $("#"+id_modal).find("#notation_" + id_question).find('input[name="valeur_question2"]').val();
						} else if(type == "qcm"){
                            $("#"+id_modal).find("#groupe_qcm-" + id_question).find("input").each(function () {
								qcm += ($(this).val()) + ';';
								if($(this).attr("class") == "form-control is_true"){
									right_answers += ($(this).val()) + ';';
								}
							});
							qcm.slice(0,-1);
							right_answers.slice(0,-1);
						}
						$.post("/question/add_question", {
							index: index,
							question: question,
							id_questionnaire: parseInt(data),
							type: type,
							emojis: emojis,
							select: select,
							qcm: qcm,
							right_answers: right_answers,
							max_range: parseInt(max_range),
							min_range: parseInt(min_range)
						});
					});
					//Ajout template si selectionné
					if(template){
						var nom_template = $("#"+id_modal).find(".template_name").find("input").val();
						$.post("/template/add", {nom: nom_template, id_questionnaire: parseInt(data)}, function(data){
							if(data != "nok"){
								var $modal_template =$('<div />',{html:modal_template});
                                $("#"+id_modal).find("#select_template").append("<option class='template_select' value='"+data+"'>"+nom_template+"</option>");
								$modal_template.find("#select_template").append("<option class='template_select' value='"+data+"'>"+nom_template+"</option>");
								modal_template = $modal_template.html();
							}
						});
					}
					//On ajoute le questionnaire au DOM.
					var ajout = '<tr class="row_questionnaire ligne_questionnaire add_hover"'
								+'id="questionnaire_'+data+'" data-href="/questionnaire/view/'+data+'">'
						+'<td class="petit-td text_center">'
						+'<input type="checkbox" class="checkbox_questionnaire" data-numero="'+data+'"/>'
						+ '</td>'
						+'<td class="view_questionnaire" data-href="/questionnaire/view/'+data+'">'
						+'<span>'+title+'</span>'
					+'</td>'
					+'<td class="view_questionnaire" data-href="/questionnaire/view/'+data+'">'
					+'<span>0 réponse</span>'
					+'</td>'
					+'<td class="paging_row_home text_center view_questionnaire" data-href="/questionnaire/view/'+data+'">'
					+	'<span>A l\'instant</span>'
					+	'</td> </tr>';
					if($('.table_questionnaire_body').length && $(".get_to_questions").length){
						$('.table_questionnaire_body').prepend(ajout);
					}
					//On ferme le modal et on le reset (sans toutes les questions et cie)
					$("#form_ajout_questionnaire").modal("hide");
					$("#form_ajout_questionnaire").html(modal_template);
					//Set du titre et des emojis du premier selecteur d'emojis, déjà présent sur la page
					$('.popover-emoji').attr("title", "<strong>Choisissez un nouvel emoji</strong>");
					$('.popover-emoji').attr("data-content", liste_emojis);
					activatePopovers()
                    $(".datepick").datetimepicker({locale: 'fr'});
                }
			});
		}
	});

	//On charge le template choisi ici
	$("body").on("change", "#select_template", function(){
		var id = $(this).val();
		$.post("/template/getHTML", {id:parseInt(id)}, function(data){
			if(data != ""){
				$("#all_questions").toggle("blind", function(){
					$("#all_questions").remove();
					$("#questions_questionnaire").append(data);
					$(".div_question").each(function(){
						$(this).show();
					});
					$("#all_questions").toggle("blind", function(){
						$('.popover-emoji').attr("title", "<strong>Choisissez un nouvel emoji</strong>");
						$('.popover-emoji').attr("data-content", liste_emojis);
						activatePopovers();
					});
				});
			}
		});
	});

	//Set du titre et des emojis du premier selecteur d'emojis, déjà présent sur la page
	$('.popover-emoji').attr("title", "<strong>Choisissez un nouvel emoji</strong>");
	$('.popover-emoji').attr("data-content", liste_emojis);

	//Quand on veut faire d'un questionnaire un template, affichage de l'input pour indiquer son nom
	$('body').on("click", "#template, #template2", function(){
		if($(this).is(':checked')){
			if($(this).attr("id") == "template") {
                $('#template_name').fadeIn();
            }else{
                $('#template_name2').fadeIn();
			}
		}else{
            if($(this).attr("id") == "template") {
                $('#template_name').fadeOut();
            }else{
                $('#template_name2').fadeOut();
            }
		}
	});

	//On sauvegarde quelle dossier va être affecté dans un input hidden dès qu'on clique sur l'edition ou sa suppression
	$("body").on("click", ".delete, .edit", function(){
		var id = $(this).parent().parent().attr("data-id");
		$("#dossier_change").val(id);
	});

	//Suppression du dossier et répercussion sur le bouton archivage
	$("body").on("click", ".delete_dossier", function(){
		var id = $("#dossier_change").val();
		$("#dossier"+id).remove();
		$("#arch_"+id).remove();
		var folder_id = parseInt(id);
         $.post('/folder/delete',
          {
           folder_id :folder_id
          });
	});

	//Editing du nom du dossier et répercussion sur le bouton archivage
	$("body").on("click", ".edit_dossier", function(){
		var id = $("#dossier_change").val();
		var titre = $(this).parent().parent().find("input").val();
		$(this).parent().parent().find("input").val("");
		$("#dossier"+id).find(".dossier_name").text(titre);
		$("#arch_"+id).text(titre);
		var folder_id = parseInt(id);
         $.post('/folder/rename',
          {
          title: titre,
          folder_id : folder_id
          });
	});

	//Edit du nom du dossier et repercussion sur le bouton archivage (quand on appuie sur entrée et qu'on clique pas)
	$("#form_edit_dossier").on("submit", function(){
		var id = $("#dossier_change").val();
		var titre = $("#titre").val();
		$("#titre").val("");
		$("#dossier"+id).find(".dossier_name").text(titre);
		$("#arch_"+id).text(titre);
		$("#edit").modal('hide');
		var folder_id = parseInt(id);
         $.post('/folder/rename',
          {
          title: titre,
          folder_id : folder_id
          });
	});

	//Ajout d'un nouveau dossier et ajout de ce dernier sur le DOM + repercussion sur le pop up d'archivage
	$(".add_dossier").on("click", function(){
		var titre = $(this).parent().parent().find("input").val();
		$(this).parent().parent().find("input").val("");
		ajoutDossierDom(titre);
	});

	//Ajout d'un dosseir comme pour le clic, sauf adapté pour quand on appuie sur entrée...
	$("#form_add_dossier").on("submit", function(){
		var titre = $(this).parent().parent().find("input").val();
		$(this).parent().parent().find("input").val("");
		ajoutDossierDom(titre);
	});

	//Desarchivage des dossiers
	$("body").on("click", ".remove_from_folder", function(){
        var id_folder = $(".current_categorie").attr("data-id");
        var questionnaires = [];
        $(".checkbox_questionnaire").each(function () {
            if (this.checked == true) {
                questionnaires.push(parseInt($(this).attr("data-numero")));
            }
        });
        questionnaires.forEach(function (id) {
            $.post('/folder/removeFromFolder',
                {
                    id_folder: parseInt(id_folder),
                    id: id
                }, function(data){
                    if(data != "nok"){
                        if($(".get_to_questionnaire").length > 0){
                            $.notify("Commentaire(s) bien retiré(s) du dossier", "success");
                        }else {
                            $.notify("Questionnaire(s) bien retiré(s) du dossier", "success");
                        }
                        $("#dossier"+id_folder).click();
                    }
                });
        });
	});

    //Ajout de questionnaire à un dossier
	$("#btn-archiver").click(function(){
        var id_dossier = $("#dossiers").val();
        if(id_dossier == 0){
            $.notify("Veuillez choisir un dossier parmi ceux que vous avez créés", "warn");
        }
        if(id_dossier != 0) {
            var questionnaires = [];
            $(".checkbox_questionnaire").each(function () {
                if (this.checked == true) {
                    questionnaires.push(parseInt($(this).attr("data-numero")));
                }
            });
            if(questionnaires.length == 0){
                if($(".get_to_questionnaire").length > 0){
                    $.notify("Veuillez sélectionner un ou plusieurs commentaires", "warn");
                }else {
                    $.notify("Veuillez sélectionner un ou plusieurs questionnaires", "warn");
                }
			}else{
				questionnaires.forEach(function (id) {
					$.post('/folder/addToFolder',
						{
							id_folder: parseInt(id_dossier),
							id: id
						}, function(data){
						if(data != "nok"){
							if($(".get_to_questionnaire").length > 0){
								$.notify("Commentaire(s) bien archivé(s)", "success");
							}else {
								$.notify("Questionnaire(s) bien archivé(s)", "success");
							}
							$("#archiver").modal("hide")
						}
						});
				});
            }
        }
        });


    //Affichage du contenu d'un dossier
    $("body").on("click", ".showFolder", function(){
			var type = "questionnaire";
			if($(".get_to_questionnaire").length > 0){
				type = "commentaire";
			}
            var id = parseInt($(this).attr("data-id"));
            var t = this;
            var module_id = parseInt($("#module_id").val());

            $.post('/folder/show',
                {
                 folder_id: id,
				 module_id: parseInt($("#module_id").val()),
				type: type
                }, function(data){
            	$(".current_categorie").find(".glyphicon-folder-open").attr("class", "glyphicon glyphicon-folder-close");
                $(".current_categorie").removeClass("current_categorie");
                $(t).addClass("current_categorie");
                $(t).find(".glyphicon-folder-close").attr("class", "glyphicon glyphicon-folder-open");
                $("#tableToReload").html(data);
                $(".remove_from_folder").remove();
                var ajout1 = '<div class="btn-group btn-head remove_from_folder" role="group" data-folder="'+id+'">'
                    + '<button type="button"'
                    +' class="btn btn_head_table add_hover archive"'
                    +'>'
                    +    '<div class="btn_head_table_text">'
                    +    '<span class="glyphicon glyphicon-level-up" '
                    + 'aria-hidden="true"></span>'
                    +'    <span class="btn_head_additional_text archive">Retirer du dossier</span>'
                    +'    </div>'
                    +'    </button>'
                    +'    </div>';
                var ajout2 = '<div class="btn-group btn-head-mobile add_hover remove_from_folder"'
                    + ' role="group">'
                    + '   <div class="btn_head_table_mobile">'
                    + '   <div class="btn_head_table_text">'
                    + '   <span class="glyphicon glyphicon-level-up" '
                    +'aria-hidden="true"></span>'
                    +'    </div>'
                    +'    </div>'
					+'   </div>';
                if(id != 0) {
                    $(".mobile_instructions_questionnaire").append(ajout2);
                    $(".instructions_questionnaire").append(ajout1);
                }
				$("*[data-time]").each(function(){
					$(this).text(moment($(this).attr("data-time")).fromNow());
				});
            });
        	});

	//Ajout d'un nouveau live
	$("body").on("click", ".add_live", function(){
		resetErrors();
		var titre = $("#titre_live").val();
		if(titre == ""){
			$("#titre_live").parent().addClass("has-error");
			$("#titre_live").parent().find("label").addClass("text-red");
		}else{
			$.post("/questionnaire/add_live", {titre: titre, module_id: parseInt($("#module_id").val())}, function(data){
				if(data != "nok"){
					$("#form_live_questionnaire").modal('hide');
					document.location.href = "/questionnaire/view/"+data;
				}
			})
		}
	});
	/*
	PARTIE HOME ELEVE (COMMENTAIRE SUR UN COURS, REPONSE A UN QUESTIONNAIRE, ...)
	 */

	//Chargement du modal answer
	$("body").on("click", ".btn_answer_or_see", function(){
		var id = parseInt($(this).attr("data-questionnaire"));
		$("#modal_answer").hide();
		$("#loading_ajax").show();
		$.post("/questionnaire/getAnswerModal", {id: id}, function(data){
			if(data != "nok"){
				$("#loading_ajax").hide();
				$("#modal_answer").html(data);
                $("#modal_answer").show();
			}
		});
	});

	/* Gestion de la réponse à un questionnaire */
	$("body").on("click", ".student_answer", function(){
		//Reset si y'avait des erreurs avant.
		resetErrors();
		var id_questionnaire = $(this).attr("data-id");
		var error = false;
		//Check des réponses aux questions notations (si dans les bornes...)
		$("#questionnaire_modal").find("input, textarea, select").each(function(){
			var id_question = $(this).attr("data-question");
			if($(this).attr("type") == "number" && $(this).val() != ""
				&& ($(this).val() < parseInt($("#min"+id_question).val()) || $(this).val() > parseInt($("#max"+id_question).val()))){
				$(this).parent().addClass("has-error");
				$(this).parent().find("label").addClass("text-red");
				$(this).parent().find("span").addClass("text-red");
				error = true;
			}
		});
		//Si OK, ajout des réponses
		if(!error) {
			var qcms = [];
			$("#questionnaire_modal").find("input, textarea, select").each(function () {
				var repond = true;
				if ($(this).attr("type") != "hidden" && (($(this).attr("type") == "radio" && $(this).is(":checked")) || $(this).attr("type") != "radio")) {
					var id_question = $(this).attr("data-question");
					var answer = $(this).val();
					if($(this).attr("type") == "checkbox"){
						if(qcms.indexOf(id_question) == -1){
							qcms.push(id_question);
							answer = "";
							$("input[type='checkbox']:checked").each(function(){
								if($(this).attr("data-question") == id_question) {
									answer += $(this).val() + ";";
								}
							});
							answer = answer.slice(0,-1);
						}else{
							repond = false;
						}
					}
					if(repond) {
						$.post("/answer/add", {id_question: id_question, answer: answer});
					}
				}
			});
			$("#" + id_questionnaire).find(".panel-default").addClass("alreadyAnswered");
			$("#btn_questionnaire"+id_questionnaire).text("Voir mes réponses");
			$("#questionnaire_modal").modal('hide');
		}
	});

	//Envoyer un commentaire sur un module
	$("#envoie_commentaire").click(function(){
		//Récupération du titre et du commentaire
		var titre = $(this).parent().parent().find("input");
		var commentaire = $(this).parent().parent().find("textarea");
		var module_id = parseInt($("#commentaire_modal").attr("data-cours"));
		var error = false;
		resetErrors();
		//Check si tout est bien rempli, sans quoi on ne fait rien et on l'indique juste
		if(commentaire.val() == ""){
			commentaire.parent().addClass("has-error");
			commentaire.parent().find("label").addClass("text-red");
			error = true;
		}if(titre.val() == ""){
			titre.parent().addClass("has-error");
			titre.parent().find("label").addClass("text-red");
			error = true;
		}
		//Si tout est OK
		if(!error){
			//On ajoute le commentaire
			$.post("/commentaire/add", {titre:titre.val(), commentaire: commentaire.val(), module_id: module_id}, function(data){

			});
			//Reset du pop up et disparition de ce dernier
			$("#commentaire_modal").modal("hide");
			commentaire.val("");
			titre.val("");
		}
	});

	/* PARTIE HOME ADMIN */
	$("body").on("click", ".delete_selection_admin", function(){
        $('.checkbox_questionnaire:checkbox:checked').each(function(){
            $.post("/questionnaire/delete", {id:parseInt($(this).attr("data-numero"))});
        });
        $('.checkbox_questionnaire:checkbox:checked').parent().parent().remove();
	});

	/*
	PARTIE HOME PROF (ARCHIVAGE, SUPPRESSION D'UN QUESTIONNAIRE, VOIR UN QUESTIONNAIRE, ...)
	 */

	//Voir un questionnaire au clique sur ce dernier
	$("body").on("click", ".view_questionnaire", function(){
		//On indique que les dernières réponses sont vu, puis on redirige
		$.post("/questionnaire/vu", {id_questionnaire: parseInt($(this).parent().attr("data-id"))}, function(data){});
		window.location = $(this).data('href');
	});

	//Check tout (pour tableau de questionnaires et tableau de commentaires...)
	$(".check_all").click(function(){
		if(this.checked){
			$(".checkbox_questionnaire").each(function(){
				if(this.checked == false){
					$(this).trigger("click")
				}
			})
		}else{
			$(".checkbox_questionnaire").each(function(){
				if(this.checked == true){
					$(this).trigger("click")
				}
			})
		}
	});

	//Gestion du check des questionnaires dans le tableau (aussi utile pour les commentaires)
	$("body").on("click", ".checkbox_questionnaire", function(){
		var numeroQuestion = $(this).attr("data-numero");
		if($(this).is(":checked")){
			$("#questionnaire_"+numeroQuestion).attr("class", $("#questionnaire_"+numeroQuestion).attr("class")+" is_checked");
		}else{
			$("#questionnaire_"+numeroQuestion).removeClass("is_checked");
		}
	});


	//Delete des questionnaires sélectionnés
	$(".delete_selection").click(function(){
		$('.checkbox_questionnaire:checkbox:checked').each(function(){
			$.post("/questionnaire/delete", {id:parseInt($(this).attr("data-numero"))});
		});
		$('.checkbox_questionnaire:checkbox:checked').parent().parent().remove();
		$(".check_all").checked("false")
	});

	//Set du div scrollable pour la home prof
	if ( $('.scrollableDiv_home_prof').length ){
		$(".scrollableDiv_home_prof").height((window.innerHeight-$(".header").outerHeight()-$('.header_home_prof').outerHeight() - 20));
		$( window ).resize(function() {
			$(".scrollableDiv_home_prof").height((window.innerHeight-$(".header").outerHeight()-$('.header_home_prof').outerHeight() - 20));
		});
	}

	/* PARTIE LIVE */

	//Stopper ou reprendre le live
	$("body").on("click", ".btn_live_off", function(){
		var on = true;
		if($(this).attr("class") == "btn btn-danger btn_live_off"){
			on = false;
		}
		var t = this;
		$.post("/questionnaire/changeStatus", {id: parseInt($("#id_questionnaire").val()), bool: on}, function(data){
			if(data != "nok"){
				if(on == false){
					$(t).attr("class", "btn btn-success btn_live_off");
					$(".text-btn_live_off").text("Redémarrer le live");
				}else{
					$(t).attr("class", "btn btn-danger btn_live_off");
					$(".text-btn_live_off").text("Arrêter le live");

				}
			}
		});
	});

	//Au clic, changement de la classe du commentaire lu
	$("body").on("click", ".live_prof_comment", function(){
		var className = $(this).attr("class");
		if(className == "panel panel-danger panel_live_comment live_prof_comment"){
			$(this).attr("class", "panel panel-default panel_live_comment live_prof_comment");
		}
	});

	//création de tous les wordcloud
	$(".wordCloud").each(function(){
		var id = $(this).attr("data-question");
		var t = this;
		$.post("/question/getDataWordCloud", {id: parseInt(id)}, function(data){
			if(data != "nok"){
				$(t).jQCloud(JSON.parse(data), {
					autoResize: true
				});
			}
		});
	});

	//Pour changer le type de question quand on selectionne
	$('body').on('change', "#select_element", function () {
		var question = 1;
		var type = $(this).val();
		if($("#type_question_"+question).val() != "" && $("#type_question_"+question).val() != "none"){
			$("#"+$("#type_question_"+question).val()+"_"+question).hide();
		}
		$("#type_question_"+question).val(type);
		$("#"+type+"_"+question).fadeIn();
	});

	//Utile pour "reset" la partie ajout d'élément...
	var add_el_template = $("#add_el_div").html();

	//Réponse au live pour l'élève
	$("body").on("click", ".btn-live-eleve", function(){
		resetErrors();
		var id_question = $(this).attr("data-question");
		var type = $(this).attr("data-type");
		var error = false;
		var parent = $(this).parent();
		if(type == "en_un_mot"){
			var value = parent.find("input").val();
			if(value == "" || value.split(' ') > 1){
				error = true;
				parent.find("input").parent().addClass("has-error");
			}
		}else if(type == "notation"){
			var max = parseInt($("#max"+id_question).val());
			var min = parseInt($("#min"+id_question).val());
			var value = $("#value"+id_question).val();
			if(value > max || value < min){
				error = true;
				$("#value"+id_question).parent().addClass("has-error");
			}
		}else if(type == "roti" || type == "qcm"){
			var auMoinsUn = false;
			parent.find("input").each(function(){
				if($(this).is(":checked")){
					auMoinsUn = true;
				}
			});
			if(!auMoinsUn){
				error = true;
				parent.find("input").each(function(){
					$(this).parent().addClass("has-error");
				});
			}
		}
		if(!error){
			var qcms = [];
			parent.find("input, textarea, select").each(function(){
				var repond = true;
				if ($(this).attr("type") != "hidden" && (($(this).attr("type") == "radio" && $(this).is(":checked")) || $(this).attr("type") != "radio")) {
					var id_question = $(this).attr("data-question");
					var answer = $(this).val();
					if($(this).attr("type") == "checkbox"){
						if(qcms.indexOf(id_question) == -1){
							qcms.push(id_question);
							answer = "";
							$("input[type='checkbox']:checked").each(function(){
								if($(this).attr("data-question") == id_question) {
									answer += $(this).val() + ";";
								}
							});
							answer = answer.slice(0,-1);
						}else{
							repond = false;
						}
					}
					if(repond) {
						$.post("/answer/add_live", {id_question: id_question, answer: answer}, function(data){
							if(data != "nok"){
								$("#panel"+id_question).toggle("slide");
								$('#elements_live').prepend(data);
								if(type != "slider" && type != "en_un_mot" && type != "instruction") {
									addChart();
								}							}
						});
					}
				}
			});
		}
	});

	//Ajout de l'élément au live
	$('body').on("click", "#btn-add-el-live", function(){
		resetErrors();
		var question = $("#titre_question").val();
		var type = $("#type_question_1").val()
		var error = false;
		if(question == ""){
			$("#titre_question").parent().addClass("has-error");
			error = true;
		}if(type == "none"){
			$("#type_question_1").parent().addClass("has-error");
			error = true;
		}if(type == "selecteur"){
			$("#groupe_select-1").find("input").each(function(){
				if($(this).val() == "") {
					$(this).parent().addClass("has-error");
					$("#selecteur_1").find(".label_question_modal").addClass("text-red");
					error = true;
				}
			});
		}if(type == "qcm"){
			/*var auMoinsUnJuste = false;*/
			$("#groupe_qcm-1").find("input").each(function(){
				if($(this).val() == "") {
					$(this).parent().addClass("has-error");
					$("#qcm_1").find(".label_question_modal").addClass("text-red");
					error = true;
				}/*if($(this).attr("class") == "form-control is_true"){
					auMoinsUnJuste = true;
				}*/
			});
			/*if(!auMoinsUnJuste){
				$("#groupe_qcm-1").addClass("has-error");
				$("#qcm_1").find(".label_question_modal").addClass("text-red");
				error = true;
			}*/
		}if(type == "notation"){
			var val1 = $("#notation_1").find("input[name='valeur_question']").val();
			var val2 = $("#notation_1").find("input[name='valeur_question2']").val();
			if(val1 == "" || val2 == "" || parseInt(val1) < 0 || parseInt(val2) < 0 || parseInt(val1) > parseInt(val2)){
				$("#notation_1").find("input[name='valeur_question']").parent().addClass("has-error");
				$("#notation_1").find("input[name='valeur_question2']").parent().addClass("has-error");
				$("#notation_1").find(".label_question_modal").addClass("text-red");
				error = true;
			}
		}if(type == "instruction"){
			if($("#instruction_textarea").val() == ""){
				$("#instruction_textarea").parent().addClass("has-error");
				error = true;
			}
		}if(!error) {
			var id_questionnaire_live = $("#id_questionnaire").val();
			var numero = parseInt($("#index_question").val())+1;
			$("#index_question").val(numero.toString());
			var id_question = 1;
			var question = $("#titre_question").val();
			var emojis = "";
			var select = "";
			var right_answers = "";
			var qcm = "";
			var min_range = "";
			var max_range = "";
			var instruction = "";
			if (type == "selecteur") {
				$("#groupe_select-" + id_question).find("input").each(function () {
					select += ($(this).val()) + ';';
				});
				select.slice(0, -1);
			} else if (type == "slider") {
				emojis = $("input[name='tres_mauvais_" + id_question + "']").val() + "," + $("input[name=mauvais_" + id_question + "]").val() + "," + $("input[name='mitige_" + id_question + "']").val() + "," + $("input[name='heureux_" + id_question + "']").val() + "," + $("input[name='tres_heureux_" + id_question + "']").val();
			} else if (type == "notation") {
				min_range = $("#notation_" + id_question).find('input[name="valeur_question"]').val();
				max_range = $("#notation_" + id_question).find('input[name="valeur_question2"]').val();
			} else if (type == "qcm") {
				$("#groupe_qcm-" + id_question).find("input").each(function () {
					qcm += ($(this).val()) + ';';
					if ($(this).attr("class") == "form-control is_true") {
						right_answers += ($(this).val()) + ';';
					}
				});
				qcm.slice(0, -1);
				right_answers.slice(0, -1);
			} else if (type=="instruction"){
				instruction += $("#instruction_textarea").val();
			}
			$.post("/question/add_question_live", {
				index: parseInt(numero),
				question: question,
				id_questionnaire: parseInt(id_questionnaire_live),
				type: type,
				emojis: emojis,
				select: select,
				qcm: qcm,
				instruction: instruction,
				right_answers: right_answers,
				max_range: parseInt(max_range),
				min_range: parseInt(min_range)
			}, function(data){
				if(data != "nok"){
					$("#add_el_div").toggle("slide", function(){
						$("#add_el_div").html(add_el_template);
						$("#add_el_div").toggle("slide", {direction: "right"}, function(){
							//Set du titre et des emojis du premier selecteur d'emojis, déjà présent sur la page
							$('.popover-emoji').attr("title", "<strong>Choisissez un nouvel emoji</strong>");
							$('.popover-emoji').attr("data-content", liste_emojis);
							activatePopovers();
						});

					});
					$('#elements_live').prepend(data);
					if(type != "slider" && type != "en_un_mot" && type != "instruction") {
						addChart();
					}
					if(type == "en_un_mot"){
						addWordCloud();
					}
				}
			});
		}
	});

	//Ajouter un commentaire au live
	$("body").on("click", "#addCommentLive", function(){
		var id_questionnaire = parseInt($("#id_questionnaire").val());
		var message = $("#instruction_textarea").val();
		if(message != "") {
			$.post("/commentaireLive/add", {id_questionnaire: id_questionnaire, message: message}, function (data) {
				if (data != "nok") {
					$(".commentLivePanel").toggle("slide", function(){
						$("#instruction_textarea").val("");
						$(".commentLivePanel").toggle("slide", {direction: "right"});
						$(".allCommentsModals").prepend(data);
						$(".panel_live_comment").eq(0).toggle("bind");
						$(".panel_live_comment").eq(0).find(".panel-heading").text(moment().fromNow());
					});
				}
			});
		}
	});

	//Lance l'update du live toutes les secondes ici.
	if($(".btn_live_off").length){
		setInterval(updateLive, 5000);
	}

	if($(".liveDiv").length){
		setInterval(updateLiveEleve, 5000);
	}

	/*
	PARTIE LISTE DES COMMENTAIRES / BOITE A IDEES (VOIR UN COMMENTAIRE, ...)
	 */

	$("body").on("click", ".voir_commentaire", function(){
		window.location = $(this).data('href');	//Redirection
	});

    //Delete des questionnaires sélectionnés
    $(".delete_selection_comment").click(function(){
        $('.checkbox_questionnaire:checkbox:checked').each(function(){
            $.post("/commentaire/delete", {id:parseInt($(this).attr("data-numero"))});
        });
        $('.checkbox_questionnaire:checkbox:checked').parent().parent().remove();
    });

	/* MODULE SELECTOR */

	//On enregistre l'id du module à delete si l'user confirme son choix
	$("body").on("click", ".btn-module", function(){
		$("#module_to_delete").val($(this).attr("id"));
	});

	//Suppression du module à l'aide de la value de l'input changée juste au-dessus
	$("body").on("click", ".delete_module", function(){
	        var id = parseInt($("#module_to_delete").val());
	        $.post('/module/delete',
                {
                    id: id,

                }, function() {
					$("#"+$("#module_to_delete").val()).toggle("bind", function() {
						$("#" + $("#module_to_delete").val()).remove();
						$("#module_to_delete").val("");
						if ($(".btn-module").length == 0) {
							$("#no-module").fadeIn();
						}
					});
		        });
	});


	/*
	 PARTIE PROFIL
	 */

	//trigger photo profil selector
	$(".edit_picture").click(function(){
		$("#changePhotoProfil").trigger("click");
	});

	//Upload photo profil
    $("#changePhotoProfil").on("change", function(){
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            var file = this.files[0]
            var extension = file['name'].substr((file['name'].lastIndexOf('.') + 1));
            if (extension === 'jpg' || extension === 'jpeg' || extension === 'gif' || extension === 'png' || extension === 'bmp') {
                reader.onload = function (e) {
                    $(".avatar").attr("src", e.target.result);
                    $.ajax({
                        url: "/uploadPhoto",
                        type: "POST",
                        data: new FormData($("#upload-file-form")[0]),
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false,
                        cache: false
                    });
                };
                reader.readAsDataURL(this.files[0]);
            }
        }
	});

	//Cherche info sur le profil
	$(".get_retour_cours").click(function(){
		var id = parseInt($(this).attr("data-cours"));
		$.post("/module/getRetour", {id: id}, function(data){
            var json = JSON.parse(data);
            if(data != "") {
            	var commentaire = json.commentaire;
            	var note = json.note;
            	if(note == ""){
            		note = "Pas encore de note";
				}if(commentaire == ""){
            		commentaire = "L'enseignant n'a pas encore fait de retours sur ce cours";
				}
                $("#commentaire_prof_cours").text(commentaire);
                $("#note_module").text(note);
            }
		});
	});

	//Notation d'un cours (dans le profil), on sauvegarde le cours qu'on va noter dans un input hidden
	//(pour avoir un pop up unique et pas 1 par cours...)
	$(".btn-note").click(function(){
		$("#cours_note").val($(this).attr("id"));
		var id = parseInt($(this).attr("id"));
		$.post("/module/getRetour",
                    {id :id},
                    function(data){
					if(data != "") {
                        var json = JSON.parse(data);
                        $("#comment_1").val(json.commentaire);
                        if(json.note != "") {
                        	$("#notation_1").val(json.note);
                            $(".notation").text(json.note);
                        }else{
                            $("#notation_1").val("5");
                            $(".notation").text("5");
						}
                        $("#titre_module_note").text("Commenter la classe : " + json.titre);
                    }
                    });

	});

	//Affiche la valeur du slider lorsqu'on note une classe
	if( $(".commentaire_classe").length ){
		$(".commentaire_classe").on("change",function(){
			var value = $(this).val();
			$(this).parent().next(".notation_div").find(".notation").text(value);
		});
	}

    //Note la classe lorsque l'on clique sur envoyer
    $("#noter-cour").click(function(){
        var commentaire = $("#comment_1").val();
        var note = $("#notation_1").val();
        var module = $("#cours_note").val();
        $.post('/profile/prof/note',
            {
               commentaire: commentaire,
               note: note,
               module: module
         });
    })
	//Set du div de scroll pour le profil
	$(".scrollableDiv2").height((window.innerHeight-$(".header").outerHeight()-$('.profil_head').outerHeight()-$('.achievement_column h2').outerHeight()-20));
	$( window ).resize(function() {
		$(".scrollableDiv2").height((window.innerHeight-$(".header").outerHeight()-$('.profil_head').outerHeight()-$('.achievement_column h2').outerHeight()));
	});

	//Set du height du scrollable div du profil
	if ( $('.panel_scroll_profil').length ){
		$(".panel_scroll_profil").outerHeight($( window ).height()-$(".panel_scroll_profil").offset().top+10);
		$( window ).resize(function() {
			$(".panel_scroll_profil").outerHeight($( window ).height()-$(".panel_scroll_profil").offset().top+10);
		});
	}

	//Changement de tabs dans le profil
	$(".navtabs_profil li").on("click", function(){
		var id = $(this).attr("data-tab");
		$(".active").attr("class", "");
		$(this).attr("class", "active");
		$(".current").attr("class", "box_profil hidden");
		$("#"+id).attr("class", "box_profil current");
	});

	/*
	PARTIE RESULTAT D'UN QUESTIONNAIRE (Chart.js avec données envoyées en JSON)
	 */

	//Remplissage des graphiques en JSON
	if ( $('.chart_js').length ){

		$(".chart_js").each(function(){
			var id_question = $(this).attr("id");
			$.post("/question/getGraphData", {id_question: id_question}, function(data){
				var ctx = document.getElementById(id_question);
				var myChart = new Chart(ctx, {
					type: 'bar',
					data: JSON.parse(data),
					options: {
						scales: {
							yAxes: [{
								ticks: {
									beginAtZero:true
								}
							}]
						}
					}
				});
				if($(".btn_live_off").length || $(".liveDiv").length){
					chartsLive[parseInt(id_question)] = myChart;
				}
			});
		});
	}

	//Like d'une réponse élève
	if( $(".like_answer").length ){
		$(".like_answer span").click(function(){
			if($(this).hasClass("liked")){
				$(this).removeClass("liked");
			}else{
				$(this).addClass("liked");
			}
		});
	}

	/*
	PARTIE VOIR UN COMMENTAIRE PROF / ELEVE / ADMIN (ALERTER ADMIN, REPONSE, ...)
	 */

	//On enregistre qu'il faut alerter l'admin sur ce commentaire (ou supprimer ce signalement en fonction...)
	$(".alert-admin").click(function(){
		var glyph = $(this).find(".glyphicon").attr("class");
		$.post("/commentaire/alerte", {id:parseInt($(this).attr("data-commentaire"))}, function(data){});
		if (glyph == "glyphicon glyphicon-bell add_margin_glyph") {
			$(this).find(".glyphicon").attr("class", "glyphicon glyphicon-ok add_margin_glyph");
		} else {
			$(this).find(".glyphicon").attr("class", "glyphicon glyphicon-bell add_margin_glyph");
		}
	});

	//Réponse à un commentaire
	$("#repondre_com").click(function(){
		resetErrors();
		var reponse = $(".reponse_a_ecrire");
		var error = false;
		//Vérification qu'il y ait bien une réponse
		if(reponse.val() == ""){
			reponse.parent().addClass("has-error");
			reponse.addClass("text-red");
			error = true;
		}
		//Si OK, on enregistre la réponse
		if(!error){
			$.post("/commentaire/answer", {id: parseInt($(this).attr("data-conversation")), message: reponse.val()}, function(data){
				//Et on affiche la réponse dans le DOM.
				$(".next_message").html(data);
				$(".repondre").toggle( "slide", function(){
					$(".next_message").toggle("blind");
				});
			});
		}
	});

});

//Affichage du succès pendant 5 seconde
function achieve(){
	$( ".achievement" ).toggle( "clip" );
	timer_achievement = setTimeout(function(){$( ".achievement" ).toggle( "clip" );}, 5000);
}

//Remove de l'input select (ajout questionnaire pop-up)
function remove_select_input(){
	$(this).parent(".input-group").remove();
}

//Indique qu'une des réponses du QCM est juste
function is_true(){
	var className = $(this).parent().find("input").attr("class");
	if(className == "form-control is_true"){
		$(this).parent().find("input").attr("class", "form-control");
	}else{
		$(this).parent().find("input").attr("class", "form-control is_true")
	}
}

//Ajout d'une question à un QCM (dans pop-up questionnaire)
function addQCM(id_question){
    var id = "all_questions";
    if( $('#modification').length && $('#modification').css("display") != "none"){
        id= "all_questions2";
    }if($(".add_element_live_div").length){
        id= "add_el_div";
    }
	var ajout = '<div class="input-group select_input_question">'
				+		'<input type="text" class="form-control" name="valeur_question">'
				+		'<span class="input-group-addon remove_addon" onclick="remove_select_input.call( this );">'
				+		'<span class="glyphicon glyphicon-remove"></span></span>'
				+		'<span class="input-group-addon istrue_addon" onclick="is_true.call( this );">'
				+		'<span class="glyphicon glyphicon-ok"></span></span>'
				+		'</div>';
	$('#'+id).find('#groupe_qcm-'+id_question).append(ajout);
}

//Ajout d'un select (ajout questionnaire pop-up)
function addSelect(id_question){
    var id = "all_questions";
    if( $('#modification').length && $('#modification').css("display") != "none"){
        id= "all_questions2";
    }if($(".add_element_live_div").length){
    	id= "add_el_div";
	}
	var ajout = '<div class="input-group select_input_question">'
		+ '<input type="text" class="form-control" name="valeur_question"><span class="input-group-addon remove_addon" onclick="remove_select_input.call( this );"><span class="glyphicon glyphicon-remove"></span></span>'
		+ '</div>';
	$("#"+id).find('#groupe_select-'+id_question).append(ajout);
}

//Suppression d'une question (ajout questionnaire pop-up)
function deleteQuestion(question){
    var id = "all_questions";
    if( $('#modification').length && $('#modification').css("display") != "none"){
        id= "all_questions2";
    }if($(".add_element_live_div").length){
        id= "add_el_div";
    }
	$("#form_question_"+question).toggle("blind", function(){
		$("#"+id).find("#form_question_"+question).remove();
	});
}

//Ajout d'une question (ajout questionnaire pop-up)
function addQuestion(){
	var id = "all_questions";
    var panelClass = "panel_add";
    if($(this).parents('#modification').length){
		id= "all_questions2";
		panelClass = "panel_edit"
	}
	var num = $(this).attr("data-question");
	$(this).attr("data-question", (parseInt(num)+1));
	var ajout = '<div style="display:none" class="panel panel-primary div_question '+panelClass+'" id="form_question_'+num+'" data-id="'+num+'">'
				  + '<div class="panel-heading"><strong>Question '+num+'</strong> <span class="glyphicon glyphicon-remove delete_question" onclick="deleteQuestion('+num+');"></span></div>'
				  + '<div class="panel-body">'
				  + '<div class="col-md-1"></div>'
				  + '<div class="col-md-11 panel_form_questionnaire">'
					+	'<div class="form-group row">'
					+ 	'<div class="col-md-3 label_question_modal">'
					+  		'<label for="titre_question">Question</label>'
					+  	'</div>'
					+  	'<div class="col-md-7">'
					+    	'<input type="text" class="form-control" id="titre_question">'
					+   '</div>'
					+  '</div>'
					+  '<div class="form-group row">'
					+  	'<div class="col-md-3 label_question_modal">'
					+  		'<label for="titre">Type de question</label>'
					+  	'</div>'
					+  	'<div class="col-md-9">'
					+    	'<div class="btn-group btn-group-justified">'
					+	    	'<div class="btn-group">'
					+			  '<button type="button" class="btn btn-primary type_question" data-type="ouverte" data-question="'+num+'">Ouverte</button>'
					+		  	'</div>'
					+		  	'<div class="btn-group">'
					+			  '<button type="button" class="btn btn-primary type_question" data-type="selecteur" data-question="'+num+'">Selecteur</button>'
					+			'</div>'
					+			'<div class="btn-group">'
					+			  '<button type="button" class="btn btn-primary type_question" data-type="notation" data-question="'+num+'">Notation</button>'
					+		  	'</div>'
					+		  	'<div class="btn-group">'
					+			  '<button type="button" class="btn btn-primary type_question" data-type="slider" data-question="'+num+'">Slider</button>'
					+			'</div>'
					+			'<div class="btn-group">'
					+			  '<button type="button" class="btn btn-primary type_question" data-type="roti" data-question="'+num+'">ROTI</button>'
					+			'</div>'
					+			'<div class="btn-group">'
					+					'<button type="button" '
					+			'class="btn btn-primary type_question" '
					+				'data-type="qcm" '
					+				'data-question="'+num+'">'
					+					'QCM'
					+					'</button>'
					+					'</div>'
					+		'</div>'
					+		'<input type="hidden" id="type_question_'+num+'" name="type_question" value=""/>'
					+    '</div>'
					+  '</div>'
					+  '<div class="form-group row valeur_question" id="ouverte_'+num+'">'
					+  	'<div class="col-md-3 label_question_modal">'
					+  		'<strong>Ouverte</strong>'
					+  	'</div>'
					+  	'<div class="col-md-7">'
					+    	'Réponse sous forme de commentaire libre de la part de l\'élève'
					+    '</div>'
					+  '</div>'
					+  '<div class="form-group row valeur_question" id="selecteur_'+num+'">'
					+  	'<div class="col-md-3 label_question_modal">'
					+  		'<strong>Selecteur</strong>'
					+  	'</div>'
					+  	'<div class="col-md-7">'
					+  		'<div class="form-group row">'
					+		  	'<div class="col-md-12">'
					+			  	'<div id="groupe_select-'+num+'">'
					+			  		'<div class="input-group">'
					+			    		'<input type="text" class="form-control" name="valeur_question"><span class="input-group-addon remove_addon" onclick="remove_select_input.call( this );"><span class="glyphicon glyphicon-remove"></span></span>'
					+			    	'</div>'
					+			   '</div>'
					+			    '<button type="button" class="btn btn-primary add_question_button" onclick="addSelect('+num+');">'
					+		    		'<span class="glyphicon glyphicon-plus"></span>'
					+		    	'</button>'
					+		    '</div>'
					+		'</div>'
					+    '</div>'
					+  '</div>'
					+  '<div class="form-group row valeur_question" id="notation_'+num+'">'
					+  	'<div class="col-md-3 label_question_modal">'
					+  		'<strong>Notation</strong>'
					+  	'</div>'
					+  	'<div class="col-md-3">'
					+    	'<div class="input-group">'
					+    		'<input type="number" class="form-control" name="valeur_question" placeholder="De">'
					+    	'</div>'
					+    '</div>'
					+    '<div class="col-md-1"></div>'
					+    '<div class="col-md-3">'
					+    	'<div class="input-group">'
					+    		'<input type="number" class="form-control" name="valeur_question2" placeholder="A">'
					+    	'</div>'
					+   '</div>'
					+  '</div>'
					+  '<div class="form-group row valeur_question" id="slider_'+num+'">'
					+  	'<div class="col-md-3 label_question_modal">'
					+  		'<strong>Slider</strong>'
					+  	'</div>'
					+  	'<div class="col-md-7">'
					+  		'<div class="input-group slider_emoji">'
					+  		'<div class="col-md-11">'
					+  			'<input class="form-control input_emoji" type="range" value="50" max="100" min="0" step="5" data-question="'+num+'">'
					+  		'</div>'
					+  		'<div class="col-md-1 smiley_slider">'
					+  			'<i class="popover-emoji em em-expressionless" id="emoji-'+num+'" data-id="'+num+'" data-toggle="popover" data-html="true" data-placement="bottom"></i>'
					+  			'<input type="hidden" name="tres_mauvais_'+num+'" value="em-tired_face"/>'
			  		+			'<input type="hidden" name="mauvais_'+num+'" value="em-disappointed"/>'
				  	+			'<input type="hidden" name="mitige_'+num+'" value="em-expressionless"/>'
				  	+			'<input type="hidden" name="heureux_'+num+'" value="em-smiley"/>'
				  	+			'<input type="hidden" name="tres_heureux_'+num+'" value="em-heart_eyes"/>'
					+			'</div>'
					+    	'</div>'
					+    '</div>'
					+  '</div>'
					+  '<div class="form-group row valeur_question" id="roti_'+num+'">'
					+  	'<div class="col-md-3 label_question_modal">'
					+  		'<strong>ROTI</strong>'
					+  	'</div>'
					+  	'<div class="col-md-7">'
					+    	'Réponse où l\'élève pourra choisir une note entre 0 et 5.'
					+    '</div>'
					+  '</div>'
					+  '<div class="form-group row valeur_question" id="qcm_'+num+'">'
					+			'<div class="col-md-3 label_question_modal">'
					+			'<strong>QCM</strong>'
					+			'</div>'
					+			'<div class="col-md-7">'
					+			'<div class="form-group row">'
					+			'<div class="col-md-12">'
					+			'<div id="groupe_qcm-'+num+'">'
					+			'<div class="input-group">'
					+			'<input type="text"	class="form-control" name="valeur_question">'
					+			'<span class="input-group-addon remove_addon" onclick="remove_select_input.call( this );">'
					+			'<span class="glyphicon glyphicon-remove"></span></span>'
					+			'<span'
					+	' class="input-group-addon istrue_addon"'
					+		' onclick="is_true.call( this );"><span'
					+	' class="glyphicon glyphicon-ok"></span></span>'
					+			'</div>'
					+			'</div>'
					+			'<button type="button" class="btn btn-primary add_question_button" onclick="addQCM('+num+');">'
					+			'<span class="glyphicon glyphicon-plus"></span>'
					+			'</button>'
					+			'</div>'
					+			'</div>'
					+			'</div>'
					+			'</div>'
					+ '<div class="col-md-1"></div>'
					+ '</div>'
					+ '</div>';
	$("#"+id).append(ajout);
	$("#"+id).find("#form_question_"+num).toggle("blind");
	$('.popover-emoji').attr("title", "<strong>Choisissez un nouvel emoji</strong>");
	$('.popover-emoji').attr("data-content", liste_emojis);
	activatePopovers();
}

//Rajoute un input élève pour l'ajouter au module
function addStudentToModule(){
	var numero = $(".add_student_div").find("input").length + 1;
	var ajout = '<div class="form-group" id="new_student'+numero+'" style="display:none;">'
		+'	    <label for="number_eleve'+numero+'">Numéro de l\'éleve</label>'
		+'	    <input type="text" class="form-control" id="number_eleve'+numero+'">'
		+'	  </div>';
	$(".add_student_div").append(ajout);
	$("#new_student"+numero).toggle("bind");
}

//Pour reset les erreurs...
function resetErrors(){
	$(".has-error").removeClass("has-error");
	$(".text-red").removeClass("text-red");
}

//Pour l'ajout d'un dossier sur le DOM
function ajoutDossierDom(titre){
    var type = "questionnaire";
    if($(".get_to_questionnaire").length > 0){
        type = "commentaire";
    }
    $.post('/folder/add',
                {
                    title: titre,
                    module_id: parseInt($("#module_id").val()),
					type: type
                },function(data){
                    if($(".categorie-home").length == 1){
                		var ajout = '<div class="categorie-home showFolder"  id="dossier'+data+'" data-id="'+data+'"><span class="glyphicon glyphicon-folder-close"></span> <span class="dossier_name" id="dossier">'+titre+' </span><div class="action_dossier"><span class="delete glyphicon glyphicon-trash" data-toggle="modal" data-target="#delete"></span><span class="edit glyphicon glyphicon-pencil" data-toggle="modal" data-target="#edit"></span></div></div>'
                		var ajout2 = '<option class="file_archive" id="arch_'+data+'" value="'+data+'">'+titre+'</option>';
                		$(".btn_add_questionnaire").after(ajout);
                		$("#dossiers").append(ajout2);
                	}else{
                		var last_dossier = $(".categorie-home").eq(-2);
                		var last_select = $(".file_archive").eq(-1);
                		var id = parseInt(last_dossier.attr("data-id")) + 1;
                		var ajout = '<div class="categorie-home showFolder"  id="dossier'+data+'" data-id="'+data+'"><span class="glyphicon glyphicon-folder-close"></span> <span class="dossier_name" id="dossier">'+titre+' </span><div class="action_dossier"><span class="delete glyphicon glyphicon-trash" data-toggle="modal" data-target="#delete"></span><span class="edit glyphicon glyphicon-pencil" data-toggle="modal" data-target="#edit"></span></div></div>'
                		var ajout2 = '<option class="file_archive" id="arch_'+data+'" value="'+data+'">'+titre+'</option>';
                		last_dossier.after(ajout);
                		last_select.after(ajout2);
                	}
                	$("#add_folder").modal('hide');
                });


}

function addChart(){
	var id_question = $(".chart_js").eq(0).attr("id");
	$.post("/question/getGraphData", {id_question: id_question}, function(data){
		var ctx = document.getElementById(id_question);
		var myChart = new Chart(ctx, {
			type: 'bar',
			data: JSON.parse(data),
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero:true
						}
					}]
				}
			}
		});
		chartsLive[parseInt(id_question)] = myChart;
	});
}

function addWordCloud(){
	var id_question = $(".wordCloud").eq(0).attr("data-question");
	var t = this;
	$.post("/question/getDataWordCloud", {id: parseInt(id_question)}, function(data){
		if(data != "nok"){
			$(t).jQCloud([{text:"", weight: 1}], {
				autoResize: true
			});
		}
	})
}

function checkCharts(){
	$(".chart_js").each(function(){
		var id_question = $(this).attr("id");
		$.post("/question/getGraphData", {id_question: id_question}, function(data) {
			var ctx = document.getElementById(id_question);
			var thisChart = chartsLive[parseInt(id_question)];
			thisChart.chart.config.data = JSON.parse(data);
			thisChart.chart.config.options.animation.duration = 0;
			thisChart.update();
		});

	});
}

function checkSliders(){
	$(".qcm_answers").each(function(){
		var id_question = $(this).attr("data-id");
		var t = this;
		$.post("/question/getGraphData", {id_question: id_question}, function(data){
			var emojisData = JSON.parse(data);
			$(t).find(".emoji_div").each(function(){
				var emoji = $(this).find(".qcm_answer_title").find("i").attr("class").substring(3);
				$(this).find(".progress-bar").attr("aria-valuenow", emojisData[emoji][1]);
				$(this).find(".progress-bar").attr("style", "width:"+emojisData[emoji][1]+"%;");
				$(this).find(".progress-bar").text(emojisData[emoji][1] + " % ("+emojisData[emoji][0]+")");
			});
		});
	});
}

function checkNumbers(){
	$(".panel-question").each(function(){
		var id= $(this).attr("data-id");
		var t = this;
		$.post("/question/getInfos", {id_question: id}, function(data){
			var datas = JSON.parse(data);
			if(datas["type"] == "slider"){
				$(t).find(".resultAnswer").attr("class", "em "+datas["avg"]);
			}else{
				$(t).find(".resultAnswer").text(datas["avg"]);
			}
			$(t).find(".nbAnswer").text(datas["nbAnswer"]);
		});
	});
}

function addAnswerModals(){
	$.post("/questionnaire/answerModals", {id:parseInt($("#id_questionnaire").val())}, function(data){
		if(data != "nok"){
			var datas = JSON.parse(data);
			jQuery.each(datas, function(index, value) {
				var id = value["id"];
				var html = value["modal"];
				var type = value["type"];
				if(type == "instruction"){
					if ($("#question"+id).length == 0) {
						$('#elements_live').prepend(html);
					}
				}else {
					if ($("#panel" + id).length == 0) {
						$(".allAnswerModals").prepend(html);
					}
				}
			});
		}
	});
}

function checkWordClouds(){
	$(".wordCloud").each(function(){
		var id = $(this).attr("data-question");
		var t = this;
		$.post("/question/getDataWordCloud", {id: parseInt(id)}, function(data){
			if(data != "nok"){
				$(t).jQCloud("update", JSON.parse(data));
			}
		});
	});
}

function checkCommentTime(){
	$(".date-comment").each(function(){
		var time  = $(this).attr("data-time");
		$(this).text(moment(time).fromNow());
	});
}

function checkComments(){
	var id = parseInt($("#id_questionnaire").val());
	$.post("/commentaireLive/getComments", {id_questionnaire: id}, function(data){
		if(data != "nok"){
			var comments = JSON.parse(data);
			jQuery.each(comments, function(index, value) {
				if($("#commentaire_"+index).length == 0){
					$(".allCommentsModals").prepend(value);
					$("#commentaire_"+index).toggle("pulsate");
				}
			});
		}
	});
}

function autocomplete(){
    $.post("/module/getAutocomplete", {id: parseInt($("#module_id").val())}, function(data){
        if(data != "nok") {
            var autocomplete = data.split(",")
            $("#student_to_del").autocomplete({
                source: autocomplete
            });
            $(".ui-autocomplete").css("z-index", "10000");
        }
    });
}

function removeStudentFromModule(){
	var eleve = $("#student_to_del").val();
    var regExp = /\(([^)]+)\)/;
    var matches = regExp.exec(eleve);
    if(matches == null){
        $.notify("Veuillez utiliser l'autocomplétion pour trouver l'élève", "warn");
	}else{
    	$.post("/module/deleteStudent", {id: parseInt($("#module_id").val()),numero: parseInt(matches[1])}, function(data){
			if(data != "nok"){
                $("#student_to_del").val("");
                $.notify("L'élève a bien été retiré du module", "success");
                autocomplete();
			}else{
                $.notify("L'élève ne semble pas être dans ce module", "warn");
			}
		});
	}
}

function changeModuleName(){
	var nom = $("#title_module").val();
	if(nom != ""){
		$.post("/module/changeName", {id: parseInt($("#module_id").val()), nom: nom}, function(data){
			if(data == "ok"){
				$.notify("Le module a désormais pour nom : \""+nom+"\"", "success");
                $("#title_module").val("");
			}else{
				$.notify("Une erreur est survenu", "warn");
			}
		});
	}else{
        $.notify("Veuillez entrer un nom", "warn");
	}
}

function addStudentToModuleSubmit(){
	$(".add_student_div").find("input").each(function(){
        var num = parseInt($(this).val()) || 0;
        var t = this;
        if(num == 0){
        	$.notify($(this).val() + " n'est pas un nombre entier", "warn");
		}else{
        	$.post("/module/addStudent", {id: parseInt($("#module_id").val()), numero: num}, function(data){
        		if(data == "ok"){
                    $.notify("L'élève " + $(t).val() + " a bien été ajouté au module", "success");
				}else{
                    $.notify("L'élève " + $(t).val() + " était déjà dans le module ou n'existe pas dans notre base de données", "warn");
                }
			});
		}
	});
	$(".add_student_div").find(".form-group").slice(1).remove();
	$(".add_student_div").find("input").val("");
}

function updateLive(){
	checkCharts();
	checkSliders();
	checkWordClouds();
	checkNumbers();
	checkComments();
	checkCommentTime();
}

function updateLiveEleve(){
	checkCharts();
	checkSliders();
	checkNumbers();
	checkWordClouds();
	addAnswerModals();
	checkCommentTime();
}

function connexionLdap(){
	if(!$("#ldap").is(":checked")){
		return true;
	}
	$.post("/loginLDAP", {email: $("#email").val(), password: $("#password").val()}, function(data){
		if(data != "nok"){
			var json = JSON.parse(data);
			$("#email2").val(json["email"]);
            $("#password2").val(json["password"]);
            $("#formHidden").submit();
		}else{
            $("#email").parent().addClass("has-error");
            $("#password").parent().addClass("has-error");
		}
	});
	return false;
}

function activatePopovers() {
    $('[data-toggle="popover"]').each(function () {
        var panel = $(this).parents(".panel");
        var container = ".panel";
        if (panel.hasClass("panel_add")) {
            container = ".panel_add";
        } else if (panel.hasClass("panel_edit")) {
            container = ".panel_edit";
        } else if (panel.hasClass("add_element_live_div")) {
            container = ".add_element_live_div";
        }
        $(this).popover({
            container: container
        });
    });
}