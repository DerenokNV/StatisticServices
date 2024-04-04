package org.stock.impl.rest;

import org.stock.impl.api.Person;
import org.stock.impl.api.PersonRequest;

import org.stock.impl.person.PersonManager;
import org.stock.impl.utils.Toolkit;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("/person")
public class StatPersonRest {

  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @POST
  @Path("/add")
  public Response addPerson( PersonRequest request ) {
    if ( checkPersRequest( request ) ) {
      return Response
              .status( Response.Status.OK )
              .entity( Toolkit.MESSAGE_BAD_REST )
              .type( MediaType.APPLICATION_JSON )
              .build();
    }
    // 1. Создадим ID
    long dtPerson =  Toolkit.toMillisUTC( request.getDtOfBirth() );
    if ( dtPerson == 0 ) {
      return Response
              .status( Response.Status.OK )
              .entity( Toolkit.ERROR_DT_PARSE )
              .type( MediaType.APPLICATION_JSON )
              .build();
    }
    Person person = new Person( request.getSex(), dtPerson, request.getRegion(), request.getIncome() );

    // 2. Закинем персону в БД
    PersonManager.addPersonTable( person );

    // 3. Вызовем перерасчет Статистики по персонам
    PersonManager.calcStatistics();

    return Response
            .status( Response.Status.OK )
            .build();
  }

  private boolean checkPersRequest( PersonRequest request ) {
    return ( request.getSex() > 1 ) ||
           ( request.getDtOfBirth() == null ) ||
           ( request.getRegion() == 0 );
  }

}
