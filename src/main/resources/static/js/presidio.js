
$("#anonymizer").click(function(){
    document.getElementById("anonymizer").innerHTML = "Anonymizing...";
    document.getElementById("anonymizer").disabled = true;



    var texto=document.getElementById("textoanonimo").value;
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

            var texto=document.getElementById("textoanonimo").value;
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
                    document.getElementById("textoanonimo").value=data.text;
                    //document.getElementById("SampleForm").submit();

                    console.log(data.text)})
                .catch(error => console.log('error', error));

        } )
        .catch(error => console.log('error', error));


});