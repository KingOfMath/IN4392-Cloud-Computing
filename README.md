## Problem ##

#### Static ####

* statically partitioning the physical resource into virtual machines will lead to poor resource utilization
* Overbooking is used to improve the overall resource utilization
* Resource capping is applied to achieve performance isolation among co-located applications by guaranteeing no application can consume more resources than those allocated to it

#### dynamic ####

* application resource demand is rarely static
	* changes in overall workload
	* the workload mix
	* internal application phases and changes
* resource cap
	* too low: the application will experience SLO violations
	* too high: the cloud service provider has to pay for the wasted resources
	* avoid both: **elastic resource scaling system**

## CouldScale ##

#### Features ####

* prediction-driven elastic resource scaling system
* automatic system meeting the SLO requirements of applications running inside the could with min resource and energy cost
	* application-agnostic, light-weight online resource demand predictor
	* good prediction accuracy for a range of real world applications

A new problem: online resource demand prediction frequently makes over- and under-estimation errors. Over-estimations are wasteful, but can be corrected by the online resource demand prediction model after it is updated with true
application resource demand data. Under-estimations are much worse since they prevent the system from knowing the true application resource demand and may cause significant SLO violations. Second, co-located applications will conflict when the available resources are insufficient to accommodate all scale-up requirements.

Solution

* online adaptive padding
* reactive error correction
* predictive migration
	* resolve scaling conflicts with minimum SLO impact
* combining resource scaling with CPU voltage and frequency scaling can save energy without affecting application SLOs

hybrid approach

* signature-driven
* state-driven

* a scaling conflict prediction component
	* conflict resolution component
	* local conflict handling component
	* migration-based conflict handling component






















