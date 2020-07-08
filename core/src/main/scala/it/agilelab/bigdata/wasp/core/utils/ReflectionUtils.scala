package it.agilelab.bigdata.wasp.core.utils

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.{ClasspathHelper, ConfigurationBuilder}

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => runtimeUniverse}

/**
 * @author Nicolò Bidotti
 */
private[wasp] object ReflectionUtils {
	def getClassSymbol[T: TypeTag]: ClassSymbol = {
		val tpe = runtimeUniverse.typeOf[T]
		val clazz = tpe.typeSymbol.asClass
		
		clazz
	}
	
	def getRuntimeClass(classSymbol: ClassSymbol): Class[_] = {
		// get a runtime classloader mirror
		val runtimeMirror = runtimeUniverse.runtimeMirror(this.getClass.getClassLoader)
		
		runtimeMirror.runtimeClass(classSymbol)
	}
	
  def findSubclassesOfSealedTrait[T: TypeTag]: List[ClassSymbol] = {
		// get the class symbol
    val clazz = getClassSymbol[T]

    // ensure we're working with a sealed trait
    require(clazz.isSealed && clazz.isTrait, "Finding all subclasses only works for sealed traits!")

    // reflection on the class to find all object subtypes
    val subclasses = clazz
	    .knownDirectSubclasses
	    .toList
	    .map(_.asClass)
    
    subclasses
  }
	
	// how to be too verbose 101
	def findObjectSubclassesOfSealedTraitAssumingTheyAreAllObjects[T: TypeTag]: List[T] = {
		// get a runtime classloader mirror
		val runtimeMirror = runtimeUniverse.runtimeMirror(this.getClass.getClassLoader)
		
		// find all subclasses
		val subclasses = findSubclassesOfSealedTrait[T]
		
		// assume they're all objects and just grab the module reference
		val objects = subclasses.map(subclass => {
			  val moduleSymbol = runtimeMirror.staticModule(subclass.fullName)
			  
			  val module = runtimeMirror
				  .reflectModule(moduleSymbol)
			    .instance
					.asInstanceOf[T]
				
				module
			}
		)
		
		objects
	}

	def findAndGetIstance[T](pacKage : String) (implicit ct: ClassTag[T], typeTag: TypeTag[T] ): T = {

		val reflections = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forPackage(pacKage))
			.setScanners(new SubTypesScanner(false)))
		val classes = reflections.getSubTypesOf(ct.runtimeClass)

		require(!classes.isEmpty,"Implementation of WaspDBHelper not found.")
		require(classes.size()==1,s"Found two or more implementation of WaspDBHelper: ${classes.toArray().mkString(",")}.")

		val clazz = classes.toArray().head.asInstanceOf[Class[_]]
		clazz.getConstructor().newInstance().asInstanceOf[T]


	}


}

