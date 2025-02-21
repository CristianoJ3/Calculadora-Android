package com.example.calculadoraandroid


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoraandroid.databinding.CalculadoraAndroidActivityBinding
import org.mariuszgromada.math.mxparser.Expression

class CalculadoraAndroid : AppCompatActivity() {

    private val binding by lazy {
        CalculadoraAndroidActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotoesCalculadora()
    }

    // função para configurar os botões da calculadora
    private fun configuraBotoesCalculadora() {

        // variáveis de controle para salvar o que foi digitado na calculadora
        // e o resultado final
        val textoCalculadora = binding.textoCalculadora
        val resultadoFinal = binding.resultadoCalculo

        // lista de botões numérios
        val botoesNumericos = listOf(
            binding.oneButton,
            binding.twoButton,
            binding.threeButton,
            binding.fourButton,
            binding.fiveButton,
            binding.sixButton,
            binding.sevenButton,
            binding.eightButton,
            binding.nineButton,
            binding.zeroButton
        )

        // lista de operadores aritiméticos
        val botoesOperacoes = listOf(
            binding.plusButton,
            binding.minusButton,
            binding.timesButton,
            binding.divisionButton,
            binding.dotButton
        )

        // lista contendo os parênteses para tratamento separado
        // da lista de operações
        val parenteses = listOf(
            binding.leftParentesesButton,
            binding.rightParentesesButton
        )

        // botao para apagar o caractere anterior
        binding.deleteButton.setOnClickListener {
            textoCalculadora.text = binding.textoCalculadora.text.dropLast(1)
        }

        // Botao de limpar
        binding.cleanButton.setOnClickListener {
            textoCalculadora.text = ""
            resultadoFinal.text = ""
        }

        // Botao para calcular
        binding.equalButton.setOnClickListener {
            val expressao = textoCalculadora.text.toString()

            // utilizando o try catch para tratar possíveis exceções no cálculo
            try {
                val resultado = Expression(expressao).calculate()

                // um if para invalidar o resultado caso o valor seja NaN
                if (resultado.isNaN()) {
                    // mensagem de toast para expressões inválidas
                    Toast.makeText(this, "EXPRESSÃO INVÁLIDA!", Toast.LENGTH_SHORT).show()
                } else {
                    resultadoFinal.text = resultado.toString()
                    textoCalculadora.text = ""
                }
            } catch (e: Exception) {
                // mensagem de toast para captar erros
                Toast.makeText(this, "ERRO!", Toast.LENGTH_SHORT).show()
            }
        }

        // Adiciona número ao campo de texto
        botoesNumericos.forEach { botao ->
            botao.setOnClickListener {
                textoCalculadora.append(botao.text)
            }
        }

        // Adiciona operadores com validação
        botoesOperacoes.forEach { botao ->
            botao.setOnClickListener {
                val textoAtual = textoCalculadora.text.toString()

                // apenas adiciona operador ao cálculo se o campo de texto não
                // estiver vazio e se o caractere anterior for um número
                if (textoAtual.isNotEmpty() && textoAtual.last().isDigit()) {
                    textoCalculadora.append(botao.text)
                }
            }
        }

        // adiciona parênteses ao texto
        parenteses.forEach { botao ->
            botao.setOnClickListener {
                val textoAtual = textoCalculadora.text.toString()

                if (botao == binding.leftParentesesButton) {
                    // Permite adicionar "(" sempre
                    textoCalculadora.append(botao.text)
                } else if (botao == binding.rightParentesesButton) {
                    // Garante que só fecha parênteses se já houver um "(" antes
                    val qtdAbre = textoAtual.count { it == '(' }
                    val qtdFecha = textoAtual.count { it == ')' }

                    // if para verificar quantos de cada parênteses foram adicionados
                    // e, assim, permitir que seja adicionado um novo
                    if (qtdAbre > qtdFecha && textoAtual.isNotEmpty() && textoAtual.last() != '(') {
                        textoCalculadora.append(botao.text)
                    }
                }
            }
        }

    }
}