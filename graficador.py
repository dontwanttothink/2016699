import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('PlanificadorTiempos.csv')

print("Datos del CSV:")
print(df)

plt.figure(figsize=(10, 6))

plt.plot(df['Tamano'], df['ListPlan_ns'], marker='o', linestyle='-', label='ListPlan (ns)', color='crimson')
plt.plot(df['Tamano'], df['HeapPlan_ns'], marker='s', linestyle='--', label='HeapPlan (ns)', color='dodgerblue')

plt.xlabel('Numero de Materias (n)', fontsize=12)
plt.ylabel('Tiempo Promedio (nanosegundos)', fontsize=12)
plt.title('Comparacion de Rendimiento: ListPlan vs HeapPlan', fontsize=14, fontweight='bold')
plt.legend(fontsize=11)
plt.grid(True, which="both", ls="--", alpha=0.5)

plt.yscale('log')

plt.savefig('rendimiento_tiempos.png', dpi=300, bbox_inches='tight')
plt.show()

plt.figure(figsize=(10, 4))
plt.plot(df['Tamano'], df['ListPlanSemestres'], marker='o', label='Semestres ListPlan', color='crimson')
plt.plot(df['Tamano'], df['HeapPlanSemestres'], marker='s', label='Semestres HeapPlan', color='dodgerblue')
plt.xlabel('Numero de Materias (n)')
plt.ylabel('Cantidad de Semestres')
plt.title('Comparacion de Semestres Requeridos')
plt.legend()
plt.grid(True)
plt.savefig('rendimiento_semestres.png', dpi=300, bbox_inches='tight')
plt.show()