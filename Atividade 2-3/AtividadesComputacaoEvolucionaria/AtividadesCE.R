rm(list = ls()) # apagar variaveis

dados <- read.csv("dadosAtividade1.csv", header = F) #header fala que não tem cabeçalho
colnames(dados) <- c("Replicacao","Caso","Resultado", "Tempo") # c para formação de uma lista

dados$Caso[ dados$Caso == 1] <- "Caso1" 
dados$Caso[ dados$Caso == 2] <- "Caso2"

dados$Caso <- as.factor(dados$Caso) #casting de dados inteiro para fator
dados$Resultado <- as.numeric(as.character(dados$Resultado))


#RESULTADO
#Melhor Resultado Geral
dados[dados$Resultado == min(dados$Resultado),] # forma condicional
#Media Resultado Geral
mean(dados$Resultado)
mean(dados$Resultado[dados$Caso == "Caso1"])
mean(dados$Resultado[dados$Caso == "Caso2"])

#Desvio Padrão Geral
sd(dados$Resultado)
sd(dados$Resultado[dados$Caso == "Caso1"])
sd(dados$Resultado[dados$Caso == "Caso2"])

#Melhor Resultado do Caso 1
min(dados$Resultado[ dados$Caso == "Caso1"])
#Melhor Resultado do Caso 2
min(dados$Resultado[ dados$Caso == "Caso2"])

#Pior Resultado Geral
dados[dados$Resultado == max(dados$Resultado),] # forma condicional
#Pior Resultado do Caso 1
max(dados$Resultado[ dados$Caso == "Caso1"])
#Pior Resultado do Caso 2
max(dados$Resultado[ dados$Caso == "Caso2"])



boxplot(Resultado ~ Caso, data = dados)

t.test(Resultado~Caso,data = dados, paired = T)
#media1 != media2
t.test(Resultado~Caso,data = dados)
# media1 < media2
t.test(Resultado~Caso, data = dados, alternative="l")
#media1 > media2
t.test(Resultado~Caso, data = dados, alternative="g")


#TEMPO
#Melhor Tempo Geral
dados[dados$Tempo == min(dados$Tempo),] # forma condicional
#Media Tempo Geral
mean(dados$Tempo)
mean(dados$Tempo[ dados$Caso == "Caso1"])
mean(dados$Tempo[ dados$Caso == "Caso2"])

#Desvio Padrão Geral
sd(dados$Tempo)
sd(dados$Tempo[ dados$Caso == "Caso1"])
sd(dados$Tempo[ dados$Caso == "Caso2"])


#Melhor Tempo do Caso 1
min(dados$Tempo[ dados$Caso == "Caso1"])
#Melhor Tempo do Caso 2
min(dados$Tempo[ dados$Caso == "Caso2"])

#Pior Tempo Geral
dados[dados$Tempo == max(dados$Tempo),] # forma condicional
#Pior Tempo do Caso 1
max(dados$Tempo[ dados$Caso == "Caso1"])
#Pior Tempo do Caso 2
max(dados$Tempo[ dados$Caso == "Caso2"])


boxplot(Tempo ~ Caso, data = dados) #verificar o tempo

t.test(Tempo~Caso,data = dados, paired = T)

#media1 != media2
t.test(Tempo~Caso, data= dados)
# media1 < media2
t.test(Tempo~Caso, data = dados, alternative="l")
#media1 > media2
t.test(Tempo~Caso, data = dados, alternative="g")

#Desvio Padrao caso 1
sd(dados$Resultado[ dados$Caso == "Caso1"])
#Media do Caso 1
mean(dados$Resultado[ dados$Caso == "Caso1"])


#Desvio Padrao caso 2
sd(dados$Resultado[ dados$Caso == "Caso2"])
#Media do Caso 2
mean(dados$Resultado[ dados$Caso == "Caso2"])


xx <- c(-1.8905888043726509, -1.411568072389159, 2.1625410096783035, 1.505785198000975, 0.9707676307266828, -2.9681382115728683, 1.5981280065084835, 1.370596358890067, 4.660006242982415, -1.3113370768625057, -1.8740482700247378, 3.6087052346512936, -0.0813797779786789, 0.5643873142036924, -0.3363980721778832, 3.1541611562429734, 2.6205716113813473, -2.4046243174118502, 1.9824807019879866, 4.9307009431569595, -1.7435739828859473, -2.3351808027825154, 0.7473710581084347, 3.7009384414005746, 1.9440570287186203, -1.8660947622822617, 5.105232186092253, -3.390451152670794, 3.1337384709135163, -4.211633401456376, 5.076039224679687, 3.925757999887014, 1.4654310457721724, 4.110742756386112, -4.2715272569182785, 2.540453578956569, 2.166080743737134, 0.1986652328733962, 2.413276464188714, -4.736774514841629, -4.295261904923208, -5.060517061830368, 3.01709370676254, 0.014651691792592736, 3.233313927559675, 0.46153669179551216, 2.249593240540264, 0.6991399833426399, 1.3507802575577914, 2.2523962471496164, -0.6132999338301373, -4.28636051146279, 1.6586324992124277, 3.2643743628893, -3.3372584444801223, -4.342466174218218, 1.3854235587070738, -1.3823738101021057, 5.114322269938504, -1.4609726709599036, -0.7118178125421029, -2.9588478718555984, -3.6312598296084424, 4.4633395017900215, 4.328529722345938, -2.877989030700742, 4.5597549803135875, 1.958017280349372, -2.248694907950935, 2.683059997938857, -3.676475143028214, 5.060680322443356, -1.196486719949362, -4.8604679126566195, 2.677519213039477, -1.0554919735810344, 3.3814082335194167, -1.4652366018521925, -3.964775975517099, 4.266786254586912, -4.216477553415135, 2.3253848787711107, 0.09945059361575748, 3.955821227809884, 1.9174575489367838, 1.9562877489144777, -2.4929409614794666, 3.2815353142784476, -4.961753975472517, -3.9243224077132206, -4.692087468729122, -2.494942707222176, -3.213901986289993, -1.6294712089410108, -1.549130192680059, -4.427380731433474, 1.5813454350851632, -1.587248058715037, 2.0996337605025586, -1.8309276150299318)
rastr <- function(xx)
{
  ##########################################################################
  #
  # RASTRIGIN FUNCTION
  #
  # Authors: Sonja Surjanovic, Simon Fraser University
  #          Derek Bingham, Simon Fraser University
  # Questions/Comments: Please email Derek Bingham at dbingham@stat.sfu.ca.
  #
  # Copyright 2013. Derek Bingham, Simon Fraser University.
  #
  # THERE IS NO WARRANTY, EXPRESS OR IMPLIED. WE DO NOT ASSUME ANY LIABILITY
  # FOR THE USE OF THIS SOFTWARE.  If software is modified to produce
  # derivative works, such modified software should be clearly marked.
  # Additionally, this program is free software; you can redistribute it 
  # and/or modify it under the terms of the GNU General Public License as 
  # published by the Free Software Foundation; version 2.0 of the License. 
  # Accordingly, this program is distributed in the hope that it will be 
  # useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
  # of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
  # General Public License for more details.
  #
  # For function details and reference information, see:
  # http://www.sfu.ca/~ssurjano/
  #
  ##########################################################################
  #
  # INPUT:
  #
  # xx = c(x1, x2, ..., xd)
  #
  ##########################################################################
  
  d <- length(xx)
  
  sum <- sum(xx^2 - 10*cos(2*pi*xx))
  
  y <- 10*d + sum
  return(y)
}
print(rastr(xx))


