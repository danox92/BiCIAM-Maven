package local_search.acceptation_type;

import java.util.List;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiobjectiveHillClimbingDistance;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class Dominance {
	//---------------------------------Métodos que se utilizan en los algoritmos multiobjetivo-------------------------------------------------------//
	//Función que determina si la solución X domina a alguna de las soluciones no dominadas de una lista
	//Devuelve la lista actualizada y true si fue adicionada a la lista o false de lo contrario
	public boolean listDominance(State solutionX, List<State> list){
		boolean domain = false;
		for (int i = 0; i < list.size() && !domain ; i++) {
			//Si la solución X domina a la solución de la lista
			if(dominance(solutionX, list.get(i))){
				//Se elimina el elemento de la lista
				list.remove(i);
				if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MultiobjectiveHillClimbingDistance)&& !list.isEmpty()) MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(list);
			}
			if (!list.isEmpty() && dominance(list.get(i), solutionX))domain = true;
		}
		//Si la solución X no fue dominada
		if(!domain){
			//Comprobando que la solución no exista
			boolean found = false;
			for (int k = 0; k < list.size() && !found; k++) {
				State element = list.get(k);
				found = solutionX.Comparator(element);
			}
			//Si la solución no existe
			if(!found){
				//Se guarda la solución candidata en la lista de soluciones óptimas de Pareto
				list.add(solutionX.clone());
				if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MultiobjectiveHillClimbingDistance)) MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(list);
				
				return true;
			}
		}
		return false;
	}
	//Función que devuelve true si solutionX domina a solutionY
	public boolean dominance(State solutionX,  State solutionY)	{
		boolean dominance = false;
		int countBest = 0; 
		int countEquals = 0;
		//Si solutionX domina a solutionY
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) 
			//Recorriendo las evaluaciones de las funciones objetivo
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() > solutionY.getEvaluation().get(i).floatValue())countBest++;
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue())countEquals++;
			}
		else
			//Recorriendo las evaluaciones de las funciones objetivo
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() < solutionY.getEvaluation().get(i).floatValue())countBest++;
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue())countEquals++;
			}
		
		if((countBest >= 1) && (countEquals + countBest == solutionX.getEvaluation().size())) dominance = true;
		
		return dominance;
	} 

}