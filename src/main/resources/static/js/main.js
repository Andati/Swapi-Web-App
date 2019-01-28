$(document).ready(function() {
    //get id
    var $id = $("#profile_id").val();
    $.ajax({
      url: "/profiledetails/"+ $id + "/",
    })
      .done(function( data ) {
        if ( console && console.log ) {
          $("#homeworld").html(data.homeWorld);
          $.each(data.films, function (index, value) {
                $("#films").append('<li class="list-inline-item btn btn-sm btn-secondary">'+ value +'</li>');
              });
          $.each(data.species, function (index, value) {
               $("#species").append('<li class="list-inline-item btn btn-sm btn-secondary">'+ value +'</li>');
             });
          $.each(data.vehicles, function (index, value) {
              $("#vehicles").append('<li class="list-inline-item btn btn-sm btn-secondary">'+ value +'</li>');
            });
          $.each(data.starships, function (index, value) {
              $("#starships").append('<li class="list-inline-item btn btn-sm btn-secondary">'+ value +'</li>');
            });

          if(data.films.length == 0) {
                $("#films").append('<li class="list-inline-item">None</li>');
          }
          if(data.species.length == 0) {
                  $("#species").append('<li class="list-inline-item">None</li>');
            }
           if(data.vehicles.length == 0) {
                   $("#vehicles").append('<li class="list-inline-item">None</li>');
             }
           if(data.starships.length == 0) {
               $("#starships").append('<li class="list-inline-item">None</li>');
           }
          $('#loading').hide();
        }
      });

      $(".add_fav").click(function() {
            var $name =$("#name").html();
            console.log("Name: ", $name);
            $.post( "/add_favourite/", { name: $name })
              .done(function( data ) {
                //alert(data);
                $(".modal-body").html(data);
                $("#myModal").modal('show');
            });
            return false;
      });

      $(".remove_fav").click(function() {
              var $name =$("#name").html();
              $.post( "/remove_favourite/", { name: $name })
                .done(function( data ) {
                  $(".modal-body").html(data);
                  $("#myModal").modal('show');
              });
              return false;
        });

      $("#myModal").on("hidden.bs.modal", function () {
          var $res = $(".modal-body").html();
          if ($res.indexOf("Successfully") >= 0) {
            //reload page
            location.reload();
          }
      });
} );
