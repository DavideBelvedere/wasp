package it.agilelab.bigdata.wasp.models

import it.agilelab.bigdata.wasp.datastores.DatastoreCategory

/**
	* Base datastore model.
	*
	* @author Nicolò Bidotti
	*/
abstract class DatastoreModel[DSC <: DatastoreCategory] extends Model
