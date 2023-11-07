
$("#publicar").click(function(){
    document.getElementById("publicar").innerHTML = "Publicando...";
    document.getElementById("publicar").disabled = true;


    let titulo=document.getElementById("tituloincidencia");
    let texto=document.getElementById("phraseDiv");
    let separator="-Sp%Str$fr#ttl#&%b#f%txt%gvn-"

    let error = document.createElement('div');
    error.className="invalid-feedback";
    error.innerHTML="Por favor, llena todos los campos";

    let errorcount=0;


    if (!titulo){
        document.getElementById("titleincidencia").appendChild(error);
        document.getElementById("tituloincidencia").className="form-control is-invalid";
        errorcount++
    }

    if(!texto){
        document.getElementById("bodyincidencia").appendChild(error);
        document.getElementById("phraseDiv").className="form-control is-invalid";
        errorcount++
    }

    if (errorcount>0){
        document.getElementById("publicar").innerHTML = "publicar incidencia";
        document.getElementById("publicar").disabled = false;
        return;
    }

    titulo=titulo.value.trim();
    texto=texto.value.trim();

    if (titulo.length==0){

        document.getElementById("titleincidencia").appendChild(error);
        document.getElementById("tituloincidencia").className="form-control is-invalid";
        errorcount++;

    }

    if (texto.length==0){
        document.getElementById("bodyincidencia").appendChild(error);
        document.getElementById("phraseDiv").className="form-control is-invalid";
        errorcount++;
    }

    if (errorcount>0){
        document.getElementById("publicar").innerHTML = "publicar incidencia";
        document.getElementById("publicar").disabled = false;
        return;
    }

    //separador de los datos

    texto=titulo+" "+separator+" "+texto


    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    raw=JSON.stringify({
        "text": texto,
        "language": "en",
        "return_decision_process": false,
        "score_threshold": 0.6,
        "entities": [
            "PHONE_NUMBER",
            "US_DRIVER_LICENSE",
            "US_PASSPORT",
            "LOCATION",
            "CREDIT_CARD",
            "CRYPTO",
            "UK_NHS",
            "US_SSN",
            "US_BANK_NUMBER",
            "EMAIL_ADDRESS",
            "DATE_TIME",
            "IP_ADDRESS",
            "PERSON",
            "IBAN_CODE",
            "NRP",
            "US_ITIN",
            "MEDICAL_LICENSE",
            "URL"
        ],
        "trace": false
    });
    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };


    fetch("https://analyzer-yanc7et5cokxo.azurewebsites.net/analyze", requestOptions)
        .then(response => response.json())
        .then(data => {


            var myHeaders = new Headers();
            myHeaders.append("Content-Type", "application/json");

            raw=JSON.stringify({
                "text": texto,
                "anonymizers": {
                    "DEFAULT": {
                        "type": "replace",
                        "new_value": "<Privado>"
                    }
                },
                "analyzer_results": data});
            var requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: raw,
                redirect: 'follow'
            };


            fetch("https://anonymizer-yanc7et5cokxo.azurewebsites.net/anonymize", requestOptions)
                .then(response => response.json())
                .then(data => {

                    //cuerpooincidencia
                    var output=document.createElement("input");
                    output.id="presidioreception";
                    output.type="hidden";
                    output.name="descripcion";
                    document.getElementById("guardarincident").appendChild(output);
                    //Trim data to receive only bodytext
                    let separator="-Sp%Str$fr#ttl#&%b#f%txt%gvn-"
                    var recibido=data.text;
                    recibido=recibido.split(separator);
                    //tituloincidencia
                    var tituloinc=document.createElement("input");
                    tituloinc.id="titulopresidio";
                    tituloinc.name="titulo";
                    tituloinc.type="hidden";
                    document.getElementById("guardarincident").appendChild(tituloinc);

                    document.getElementById("titulopresidio").value=recibido[0].trim();

                    document.getElementById("presidioreception").value=recibido[1].trim();

                    document.getElementById("guardarincident").submit();
                })

                .catch(error => console.log('error', error));

        } )
        .catch(error => console.log('error', error));


});